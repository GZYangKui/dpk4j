package cn.navclub.xt.server.gateway

import cn.navclub.xt.server.AbstractEBVerticle
import cn.navclub.xt.server.enums.APPAction
import cn.navclub.xt.server.enums.HttpRequestAPI
import cn.navclub.xt.server.util.RequestDataUtil
import cn.navclub.xt.server.verticle.OBSVerticel
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.launch

class WebVerticel : AbstractEBVerticle() {

    override suspend fun start() {
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        //验证推流请求是否合法
        router.mountSubRouter("/obs", this.obsRouter())
        vertx.createHttpServer().requestHandler(router).listen(8080).await()
    }

    private fun obsRouter(): Router {
        val router = Router.router(vertx)

        router.post(HttpRequestAPI.REQUEST_PUBLISH.subPath)

        router.route().handler {
            val clazz = OBSVerticel::class.java
            val api = HttpRequestAPI.getApi(it.request().path())
            val json = if (api.action == APPAction.REQUEST_PUBLISH){
                RequestDataUtil.formToJson(it)
            }else{
                it.bodyAsJson
            }
            launch {
                it.json(requestEB(clazz,api.action,json))
            }
        }
        return router
    }
}