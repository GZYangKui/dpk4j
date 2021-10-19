package cn.navclub.xtm.kit.client;

import cn.navclub.xtm.kit.client.impl.TCPClient;
import cn.navclub.xtm.kit.client.impl.UDPClient;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XClientFactory {
    private static final Logger LOG = LoggerFactory.getLogger(XClientFactory.class);

    private final TCPClient tcpClient;
    private final UDPClient udpClient;

    public XClientFactory(XClient.XClientBuilder builder) {
        this.tcpClient = new TCPClient(builder);
        this.udpClient = new UDPClient(builder);
    }

    /**
     * 发送TCP数据包
     */
    public Future<Void> tcp(Buffer buffer) {
        return this.tcpClient.send(buffer);
    }

    /**
     * 发送UDP数据包
     */
    public Future<Void> udp(Buffer buffer) {
        return this.udpClient.send(buffer);
    }

    /**
     * 激活TCP和UDP连接
     */
    public Future<Void> connectAll() {
        var promise = Promise.<Void>promise();
        var future = this.tcpClient
                .connect()
                .compose(it -> this.udpClient.connect());
        future.onComplete(ar -> {
            if (ar.succeeded()) {
                promise.complete();
                return;
            }
            LOG.error("激活连接失败!", ar.cause());
            this.closeAll().onSuccess(promise::complete).onFailure(promise::fail);
        });
        return promise.future();
    }

    /**
     * 关闭TCP和UDP连接
     */
    public Future<Void> closeAll() {
        return this.tcpClient.close()
                .compose(it -> this.udpClient.close())
                .onFailure(t -> LOG.error("关闭TCP和UDP连接发生错误!"));
    }

    public static XClientFactory factory(XClient.XClientBuilder builder) {
        return new XClientFactory(builder);
    }
}
