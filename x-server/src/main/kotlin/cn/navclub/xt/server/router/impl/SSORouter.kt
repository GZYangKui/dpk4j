package cn.navclub.xt.server.router.impl

import cn.navclub.xt.server.AbstractEBVerticle.Companion.requestEB
import cn.navclub.xt.server.api.CommonResult
import cn.navclub.xt.server.enums.APPAction
import cn.navclub.xt.server.enums.HttpRequestAPI
import cn.navclub.xt.server.enums.TMessage
import cn.navclub.xt.server.router.SubRouter
import cn.navclub.xt.server.verticle.SSOVerticel
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class SSORouter(vertx: Vertx) : SubRouter(vertx, "/sso") {

    override fun register(vertx: Vertx, router: Router) {
        val clazz = SSOVerticel::class.java
        router.post(HttpRequestAPI.SSO_LOGIN.subPath).execute {
            val json = requestEB<CommonResult<Void>>(vertx, clazz, APPAction.USER_LOGIN, it.bodyAsJson, TMessage.HTTP)
            it.json(json)
        }
    }
}