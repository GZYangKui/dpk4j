package cn.navclub.xtm.kit.client;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
        });
        return promise.future();
    }

    /**
     * 初始化连接信息
     */
    private void init(NetSocket socket) {
        this.socket = socket;
        this.socket.exceptionHandler(t->{
            System.out.println("异常发生");
        });

        this.socket.closeHandler(v->{
            this.statusChange(XTClientStatus.BROKEN_CONNECT);
            System.out.println("连接关闭");
        });

        this.socket.handler(System.out::println);
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
                LOG.info("NetClient close failed:{}", ar.cause().getMessage());
                promise.fail(ar.cause());
            } else {
                LOG.info("NetClient is closed!");
                promise.complete();
            }
            this.builder.getVertx().close();
        });
        return promise.future();
    }

    public synchronized void addListener(XTClientListener listener){
        if (this.listeners.contains(listener)){
            return;
        }
        this.listeners.add(listener);
    }

    private void statusChange(XTClientStatus status){
        var oldStatus = this.statusRef.get();
        this.statusRef.set(status);
        for (XTClientListener service : this.listeners) {
            service.statusHandler(oldStatus,status);
        }
    }
}
