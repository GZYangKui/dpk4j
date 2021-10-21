package cn.navclub.xt.server.service.impl;

import cn.navclub.xt.server.AbstractServiceProxy;
import cn.navclub.xt.server.api.CommonResult;
import cn.navclub.xt.server.api.RestResponse;
import cn.navclub.xt.server.service.UserService;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.HashMap;
import java.util.Map;

public class UserServiceProxyImpl extends AbstractServiceProxy implements UserService {
    public UserServiceProxyImpl(Vertx vertx) {
        super(vertx);
        this.autoRegProxy(UserService.class, this);
    }

    @Override
    public Future<CommonResult<Map<String,String>>> login() {
        var map = new HashMap<String,String>();
        map.put("test","test");
        return Future.succeededFuture(RestResponse.success(map));
    }
}
