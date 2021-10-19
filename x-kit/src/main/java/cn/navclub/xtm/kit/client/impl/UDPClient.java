package cn.navclub.xtm.kit.client.impl;

import cn.navclub.xtm.kit.client.XClient;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;

/**
 * UDP 客户端
 */
public class UDPClient extends XClient {
    private final XClientBuilder builder;

    public UDPClient(XClientBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Future<Void> connect() {
        return Future.succeededFuture();
    }

    @Override
    public Future<Void> send(Buffer buff) {
        return null;
    }

    @Override
    public Future<Void> close() {
        return Future.succeededFuture();
    }
}
