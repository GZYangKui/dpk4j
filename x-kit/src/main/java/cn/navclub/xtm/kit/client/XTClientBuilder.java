package cn.navclub.xtm.kit.client;

import io.vertx.core.Vertx;

public class XTClientBuilder {
    /**
     * 默认心跳间隔
     */
    private static final int DEFAULT_INTERVAL = 3 * 1000;
    /**
     * 默认心跳允许最大失败次数为10次
     */
    private static final int DEFAULT_MAX_HB_TIMES = 10;

    private final Vertx vertx;

    private String host;
    private Integer port;
    /**
     * 心跳间隔
     */
    private int interval;
    /**
     * 最大允许心跳失败次数,如果超过该次数则判定连接不可用
     */
    private int maxHBTimes;

    public XTClientBuilder(Vertx vertx) {
        this.vertx = vertx;
        this.interval = DEFAULT_INTERVAL;
        this.maxHBTimes = DEFAULT_MAX_HB_TIMES;
    }

    public XTClientBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public XTClientBuilder setPort(Integer port) {
        this.port = port;
        return this;
    }

    public XTClientBuilder setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public XTClientBuilder setMaxHBTimes(int maxHBTimes) {
        this.maxHBTimes = maxHBTimes;
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

    public int getInterval() {
        return interval;
    }

    public int getMaxHBTimes() {
        return maxHBTimes;
    }
}