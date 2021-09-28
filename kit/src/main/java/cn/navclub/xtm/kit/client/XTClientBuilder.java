package cn.navclub.xtm.kit.client;

import io.vertx.core.Vertx;

public  class XTClientBuilder {
    private final Vertx vertx;

    private String host;
    private Integer port;

    public XTClientBuilder(Vertx vertx) {
        this.vertx = vertx;
    }

    public XTClientBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public XTClientBuilder setPort(Integer port) {
        this.port = port;
        return this;
    }

    public XTClient build() {
        return new XTClient(this);
    }

    public static XTClientBuilder newBuilder(Vertx vertx) {
        return new XTClientBuilder(vertx);
    }


    public Vertx getVertx() {
        return vertx;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}