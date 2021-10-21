package cn.navclub.xt.server.router

import cn.navclub.xt.server.AbstractEBVerticle
import cn.navclub.xt.server.api.CommonResult
import cn.navclub.xt.server.api.RestResponse
import cn.navclub.xt.server.config.QUERY_PARAMS
import cn.navclub.xt.server.enums.HttpRequestAPI
import cn.navclub.xt.server.enums.TMessage
import cn.navclub.xt.server.util.StringUtil
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class SubRouter(private val vertx: Vertx, private val name: String) {

    @OptIn(DelicateCoroutinesApi::class)
    protected fun Route.execute(executor: suspend (it: RoutingContext) -> Unit) {
        this.handler {
            GlobalScope.launch {
                executor.invoke(it)
            }
        }
    }

    /**
     * 将当前子路由挂入主路由中
     */
    fun mount(mainRouter: Router) {
        val router = Router.router(vertx)
        this.register(vertx,router)
        mainRouter.mountSubRouter(name, router)
    }


    private fun queryParams(rct: RoutingContext): JsonObject {
        val json = JsonObject()
        val queries = rct.queryParams()
        for (entry in queries) {
            json.put(entry.key, entry.value)
        }
        return json
    }

    protected fun kv2Json(str: String?): JsonObject {
        val json = JsonObject()
        if (StringUtil.isEmpty(str)) {
            return json
        }
        val arr = str!!.split("&")
        for (s in arr) {
            val index = s.indexOf("=")
            if (index <= 0 || index == s.length - 1) {
                continue
            }
            val key = s.substring(0, index)
            val value = s.substring(index + 1)
            json.put(key, value)
        }
        return json
    }

    /**
     * 将请求通过EventBus转发出去
     */
    protected suspend fun restReqEB(rct: RoutingContext, clazz: Class<*>, api: HttpRequestAPI, data: JsonObject) {
        data.put(QUERY_PARAMS, this.queryParams(rct))
        val respData = try {
            AbstractEBVerticle.requestEB<CommonResult<Any>>(vertx, clazz, api.action, data, TMessage.HTTP)
        } catch (e: Exception) {
            e.printStackTrace()
            RestResponse.error("服务器错误")
        }
        rct.json(respData)
    }

    /**
     * 将相关子路由注册到当前路由上
     */
    protected abstract fun register(vertx:Vertx,router: Router)
}