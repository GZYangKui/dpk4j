package cn.navclub.xt.server

import cn.navclub.xt.server.api.CommonResult
import cn.navclub.xt.server.api.RestResponse
import cn.navclub.xt.server.config.*
import cn.navclub.xt.server.enums.APPAction
import cn.navclub.xt.server.enums.HttpRequestAPI
import cn.navclub.xt.server.enums.TMessage
import cn.navclub.xt.server.util.StringUtil
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.launch

abstract class RestAPIVerticle : AbstractEBVerticle() {

    override suspend fun start() {
        super.start()
    }

    /**
     * 将请求通过EventBus转发出去
     */
    protected fun restReqEB(rct: RoutingContext, clazz: Class<*>, api: HttpRequestAPI, data: JsonObject) {
        val that = this
        launch {
            data.put(QUERY_PARAMS, that.queryParams(rct))
            val respData: JsonObject = try {
                that.requestEB(clazz, api.action, data, TMessage.HTTP)
            } catch (e: Exception) {
                e.printStackTrace()
                RestResponse.error<Void>("服务器错误").toJson()
            }
            rct.json(respData)
        }
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
}