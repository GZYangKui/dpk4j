package cn.navclub.xt.server.service;

import cn.navclub.xt.server.api.CommonResult;
import cn.navclub.xt.server.service.impl.UserServiceProxyImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Map;

@ProxyGen
public interface UserService {
    static UserService create(Vertx vertx) {
        return new UserServiceProxyImpl(vertx);
    }

    static UserService createProxy(Vertx vertx) {
        return new UserServiceVertxEBProxy(vertx, UserService.class.getName());
    }

    Future<CommonResult<Map<String,String>>> login();
}
