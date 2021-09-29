package cn.navclub.xtm.kit.client;

import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.encode.SocketDataEncode;
import cn.navclub.xtm.kit.enums.ClientStatus;
import cn.navclub.xtm.kit.enums.SocketCMD;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * TCP连接客户端
 *
 * @author yangkui
 */
public class XTClient {
    private static final Logger LOG = LoggerFactory.getLogger(XTClient.class);

    private final XTClientBuilder builder;

    private final List<XTClientListener> listeners;

    private volatile AtomicReference<XTClientStatus> statusRef;

    private volatile NetSocket socket;


    protected XTClient(XTClientBuilder builder) {
        this.builder = builder;
        this.listeners = new ArrayList<>();
        this.statusRef = new AtomicReference<>(XTClientStatus.NOT_CONNECT);
    }

    /**
     * 创建连接
     */
    public Future<Void> connect() {
        var promise = Promise.<Void>promise();
        var netClient = this.builder.getVertx().createNetClient();
        this.statusChange(XTClientStatus.CONNECTING);
        netClient.connect(this.builder.getPort(), this.builder.getHost(), ar -> {
            LOG.info("TCP Client Connect result:{}", ar.succeeded());
            if (ar.failed()) {
                promise.fail(ar.cause());
                this.statusChange(XTClientStatus.BROKEN_CONNECT);
                return;
            }
            this.statusChange(XTClientStatus.CONNECTED);
            this.init(ar.result());
            promise.complete();
        });
        return promise.future();
    }

    /**
     * 初始化连接信息
     */
    private void init(NetSocket socket) {
        this.socket = socket;
        var parser = RecordParser.create();
        parser.handler(record -> {
            LOG.info("Receive TCP Msg[cmd:{},addr:{},len:{}byte]", record.cmd(), record.address(), record.length());
            //投递消息
            for (XTClientListener listener : this.listeners) {
                try {
                    listener.onMessage(record);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.socket.closeHandler(v -> this.statusChange(XTClientStatus.BROKEN_CONNECT));
        this.socket.handler(parser::handle);
    }

    public Future<Void> send(SocketCMD cmd, ClientStatus clientStatus, JsonObject json) {
        var status = this.statusRef.get();
        if (status != XTClientStatus.CONNECTED) {
            return Future.failedFuture("当前连接不可用,连接状态:[" + status.getMessage() + "]");
        }
        var buffer = SocketDataEncode.encodeJson(cmd, clientStatus,0, json);
        var promise = Promise.<Void>promise();
        var future = this.socket.write(buffer);
        future.onComplete(ar -> {
            if (ar.failed()) {
                promise.fail(ar.cause());
                LOG.info("Socket data package send failed:{}", ar.cause().getMessage());
                return;
            }
            promise.complete();
        });
        return promise.future();
    }

    /**
     * 关闭连接
     */
    public Future<Void> close() {
        if (this.socket == null) {
            return Future.succeededFuture();
        }
        var promise = Promise.<Void>promise();
        this.socket.close(ar -> {
            if (ar.failed()) {
                promise.fail(ar.cause());
                LOG.info("NetClient close failed:{}", ar.cause().getMessage());
                return;
            }
            LOG.info("NetClient is closed!");
            promise.complete();
            this.builder.getVertx().close();
        });
        return promise.future();
    }

    public synchronized void addListener(XTClientListener listener) {
        if (this.listeners.contains(listener)) {
            return;
        }
        this.listeners.add(listener);
    }

    private void statusChange(XTClientStatus status) {
        var oldStatus = this.statusRef.get();
        this.statusRef.set(status);
        for (XTClientListener service : this.listeners) {
            service.statusHandler(oldStatus, status);
        }
    }
}
