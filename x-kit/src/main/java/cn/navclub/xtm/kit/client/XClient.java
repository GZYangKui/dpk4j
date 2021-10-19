package cn.navclub.xtm.kit.client;

import cn.navclub.xtm.kit.client.impl.TCPClient;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

public abstract class XClient {
    /**
     * 连接到服务器
     */
    public abstract Future<Void> connect();

    /**
     * 发送数据包到服务器
     */
    public abstract Future<Void> send(Buffer buff);

    /**
     * 关闭服务器
     */
    public abstract Future<Void> close();

    public static class XClientBuilder {
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

        public XClientBuilder(Vertx vertx) {
            this.vertx = vertx;
            this.interval = DEFAULT_INTERVAL;
            this.maxHBTimes = DEFAULT_MAX_HB_TIMES;
        }

        public XClientBuilder setHost(String host) {
            this.host = host;
            return this;
        }

        public XClientBuilder setPort(Integer port) {
            this.port = port;
            return this;
        }

        public XClientBuilder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        public XClientBuilder setMaxHBTimes(int maxHBTimes) {
            this.maxHBTimes = maxHBTimes;
            return this;
        }

        public TCPClient buildTClient() {
            return new TCPClient(this);
        }

        public static XClientBuilder newBuilder(Vertx vertx) {
            return new XClientBuilder(vertx);
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
}
