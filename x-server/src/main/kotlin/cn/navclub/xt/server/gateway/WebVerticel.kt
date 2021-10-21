package cn.navclub.xt.server.gateway

import cn.navclub.xt.server.AbstractEBVerticle
import cn.navclub.xt.server.router.impl.OBSRouter
import cn.navclub.xt.server.router.impl.SSORouter
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.launch

class WebVerticel : AbstractEBVerticle() {
    fun Route.execute(executor: suspend (it: RoutingContext) -> Unit) {
        this.handler {
            launch {
                executor.invoke(it)
            }
        }
    }

    override suspend fun start() {
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        //注册推流相关路由
        OBSRouter(vertx).mount(router)
        //注册用户相关路由
        SSORouter(vertx).mount(router)

        vertx.createHttpServer().requestHandler(router).listen(8080).await()
    }
}