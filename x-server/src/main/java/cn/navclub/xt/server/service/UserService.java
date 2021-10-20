package cn.navclub.xt.server.service;

import cn.navclub.xt.server.service.impl.UserServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Vertx;

@ProxyGen
public interface UserService {
    static UserService create(Vertx vertx){
        return new UserServiceImpl();
    }
}
