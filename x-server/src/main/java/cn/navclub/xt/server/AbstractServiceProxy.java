package cn.navclub.xt.server;

import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServiceProxy {
    private final Vertx vertx;
    protected Logger logger;

    public AbstractServiceProxy(Vertx vertx) {
        this.vertx = vertx;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    protected <T> void regProxy(Class<T> clazz, T impl, String address, long timeout, boolean topLevel) {
        var sb = new ServiceBinder(this.vertx);

        sb.setTopLevel(true);
        sb.setAddress(address);
        sb.setTimeoutSeconds(6 * 60);

        try {
            sb.register(clazz, impl);
        } catch (Exception e) {
            logger.error("Register service proxy failed.", e);
            throw new RuntimeException(e);
        }
        logger.info("Success register {} to event-bus.", address);
    }

    protected <T> void autoRegProxy(Class<T> clazz, T impl) {
        var address = clazz.getName();
        this.regProxy(clazz, impl, address, 6 * 60, true);
    }


    public Vertx getVertx() {
        return vertx;
    }
}
