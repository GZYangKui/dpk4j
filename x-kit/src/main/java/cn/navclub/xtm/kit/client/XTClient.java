package cn.navclub.xtm.kit.client;

import cn.navclub.xtm.core.decode.RecordParser;
import cn.navclub.xtm.core.encode.SocketDataEncode;
import cn.navclub.xtm.core.enums.SocketCMD;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * TCP连接客户端
 *
 * @author yangkui
 */
public class XTClient {
    private static final long DEFAULT_TIMER_ID = -1;
    private static final Logger LOG = LoggerFactory.getLogger(XTClient.class);

    private volatile NetSocket socket;

    private final XTClientBuilder builder;

    private final List<XTClientListener> listeners;

    private final AtomicInteger hbTimers;

    private final AtomicReference<XTClientStatus> statusRef;

    private final Vertx vertx;

    /**
     * 心跳定时器记录id
     */
    private volatile long timeId;
    /**
     * 重连定时器id
     */
    private volatile long reTimeId;


    protected XTClient(XTClientBuilder builder) {
        this.builder = builder;
        this.timeId = DEFAULT_TIMER_ID;
        this.reTimeId = DEFAULT_TIMER_ID;
        this.vertx = builder.getVertx();
        this.listeners = new ArrayList<>();
        this.hbTimers = new AtomicInteger(0);
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
            //心跳包
            if (record.getCmd() == SocketCMD.HEART_BEAT) {
                this.hbTimers.getAndAdd(-1);
                return;
            }
            LOG.debug(
                    "Receive TCP Package [Direction:{},Cmd:{},Source Address:{},Data Length:{} byte]",
                    record.getDirection(),
                    record.getCmd(),
                    record.getSourceAddr(),
                    record.getData().length()
            );
            //投递消息
            for (XTClientListener listener : this.listeners) {
                //判断监听器是否监听该socket命令
                if (!listener.actions().contains(record.getCmd())) {
                    continue;
                }
                Throwable ex = null;
                try {
                    LOG.debug("Delivery message to {} listener", listener.getClass().getSimpleName());
                    listener.onMessage(this, record);
                } catch (Exception e) {
                    ex = e;
                }
                if (ex != null) {
                    LOG.debug("Delivery message failed.", ex);
                } else {
                    LOG.debug("Success delivery message to {} listener.", listener.getClass().getSimpleName());
                }
            }
        });
        this.socket.closeHandler(v -> this.statusChange(XTClientStatus.BROKEN_CONNECT));
        this.socket.handler(parser::handle);
    }

    public Future<Void> send(Buffer buffer) {
        var status = this.statusRef.get();
        if (status != XTClientStatus.CONNECTED) {
            return Future.failedFuture("当前连接不可用,连接状态:[" + status.getMessage() + "]");
        }
        var promise = Promise.<Void>promise();
        var future = this.socket.write(buffer);
        future.onComplete(ar -> {
            if (ar.failed()) {
                promise.fail(ar.cause());
                this.statusChange(XTClientStatus.BROKEN_CONNECT);
                LOG.error("Socket data package send failed!", ar.cause());
                return;
            }
            promise.complete();
        });
        return promise.future();
    }

    /**
     * 关闭连接
     */
    public void close() {
        //清除定时器
        this.clearTimer();
        //清除重连定时器
        this.vertx.cancelTimer(this.reTimeId);
        this.reTimeId = DEFAULT_TIMER_ID;
        if (this.socket == null) {
            return;
        }
        this.socket.close();
        this.socket = null;
    }

    public synchronized void removeListener(XTClientListener listener) {
        Objects.requireNonNull(listener);
        if (!this.listeners.contains(listener)) {
            return;
        }
        this.listeners.remove(listener);
    }

    public synchronized void addListener(XTClientListener listener) {
        Objects.requireNonNull(listener);
        if (this.listeners.contains(listener)) {
            return;
        }
        this.listeners.add(listener);
    }

    private void statusChange(XTClientStatus status) {
        var oldStatus = this.statusRef.get();
        //连接成功=>开启心跳
        if (status == XTClientStatus.CONNECTED) {
            this.initHB();
            this.vertx.cancelTimer(this.reTimeId);
        }
        //连接断开=>停止心跳及关闭连接
        if (status == XTClientStatus.BROKEN_CONNECT) {
            this.close();
            this.startReTimer();
        }
        this.statusRef.set(status);
        for (XTClientListener service : this.listeners) {
            service.statusHandler(this, oldStatus, status);
        }
    }

    private void startReTimer() {
        if (this.reTimeId != DEFAULT_TIMER_ID) {
            return;
        }
        this.reTimeId = this.vertx.setPeriodic(1000, time -> {
            if (this.statusRef.get() == XTClientStatus.CONNECTED) {
                this.vertx.cancelTimer(this.reTimeId);
                return;
            }
            this.connect();
        });
    }

    private void initHB() {
        var vertx = this.builder.getVertx();
        //定时器存在
        if (this.timeId != DEFAULT_TIMER_ID) {
            vertx.cancelTimer(this.timeId);
        }
        //定时发送数据包
        this.timeId = vertx.setPeriodic(this.builder.getInterval(), time -> {
            //如果心跳包无响应次数超过最大允许次数
            if (this.hbTimers.get() > this.builder.getMaxHBTimes()) {
                this.clearTimer();
                this.statusChange(XTClientStatus.BROKEN_CONNECT);
                return;
            }
            var buffer = SocketDataEncode.restRequest(
                    SocketCMD.HEART_BEAT,
                    0,
                    null
            );
            this.send(buffer);
            this.hbTimers.getAndAdd(1);
        });
    }

    private void clearTimer() {
        //心跳次数重置为0
        this.hbTimers.set(0);
        if (this.timeId != DEFAULT_TIMER_ID) {
            //清除定时器
            this.builder.getVertx().cancelTimer(this.timeId);
            //重置定时器记录ID
            this.timeId = DEFAULT_TIMER_ID;
        }
    }

    public Vertx getVertx() {
        return vertx;
    }
}
