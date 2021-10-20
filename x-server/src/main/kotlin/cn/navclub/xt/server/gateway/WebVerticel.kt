package cn.navclub.xt.server.gateway

import cn.navclub.xt.server.RestAPIVerticle
import cn.navclub.xt.server.enums.APPAction
import cn.navclub.xt.server.enums.HttpRequestAPI
import cn.navclub.xt.server.enums.TMessage
import cn.navclub.xt.server.verticle.OBSVerticel
import cn.navclub.xt.server.verticle.SSOVerticel
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.launch

class WebVerticel : RestAPIVerticle() {

    override suspend fun start() {
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        //注册推流相关路由
        router.mountSubRouter("/obs", this.obsRouter())
        //注册用户相关路由
        router.mountSubRouter("/sso", this.ssoRouter())

        vertx.createHttpServer().requestHandler(router).listen(8080).await()
    }

    private fun obsRouter(): Router {
        val router = Router.router(vertx)
        val clazz = OBSVerticel::class.java
        router.post(HttpRequestAPI.REQUEST_PUBLISH.subPath).handler {
            val form = kv2Json(it.bodyAsString)
            restReqEB(it, clazz, HttpRequestAPI.REQUEST_PUBLISH, form)
        }
        return router
    }

    private fun ssoRouter(): Router {
        val router = Router.router(vertx)
        val clazz = SSOVerticel::class.java
        router.post(HttpRequestAPI.SSO_LOGIN.subPath).handler {
            launch {
                val json = requestEB(clazz, APPAction.USER_LOGIN, it.bodyAsJson, TMessage.HTTP)
                it.json(json)
            }
        }
        return router
    }
}