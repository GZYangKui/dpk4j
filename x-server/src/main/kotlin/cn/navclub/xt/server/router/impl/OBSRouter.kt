package cn.navclub.xt.server.router.impl

import cn.navclub.xt.server.router.SubRouter
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class OBSRouter(vertx: Vertx):SubRouter(vertx,"/obs") {
    override fun register(vertx: Vertx, router: Router) {

    }

}