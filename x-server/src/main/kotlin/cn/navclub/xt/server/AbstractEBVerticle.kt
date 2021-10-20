package cn.navclub.xt.server

import cn.navclub.xt.server.api.CommonResult
import cn.navclub.xt.server.api.RestResponse
import cn.navclub.xt.server.config.ACTION
import cn.navclub.xt.server.config.DATA
import cn.navclub.xt.server.config.TYPE
import cn.navclub.xt.server.enums.APPAction
import cn.navclub.xt.server.enums.HttpRequestAPI
import cn.navclub.xt.server.enums.TMessage
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.launch
import java.lang.RuntimeException

abstract class AbstractEBVerticle : CoroutineVerticle() {
    /**
     * 在start中统一注册EventBus
     */
    override suspend fun start() {
        this.vertx.eventBus().consumer<JsonObject>(this::class.java.name) {
            val json = try {
                val reqJson = it.body()
                val data = reqJson.getJsonObject(DATA)
                val type = TMessage.valueOf(reqJson.getString(TYPE))
                val action = APPAction.valueOf(reqJson.getString(ACTION))
                if (type == TMessage.INTERNAL) {
                    interReq(action, data)
                } else {
                    restReq(action, data).toJson()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                it.fail(RestResponse.ERROR, e.message)
                return@consumer
            }

            it.reply(json)
        }
    }

    /**
     * 每个Verticle根据实际需求自定义实现该方法处理程序内部事件
     */
    protected open fun interReq(action: APPAction, data: JsonObject): JsonObject {
        return JsonObject()
    }


    /**
     * 每个Verticle根据需求实现该方法处理HTTP请求
     */
    protected open fun restReq(action: APPAction, data: JsonObject): CommonResult<Any> {
        return RestResponse.success()
    }

    /**
     * 程序各个单元之间通信
     */
    protected suspend fun requestEB(clazz: Class<*>, action: APPAction, data: JsonObject,type:TMessage): JsonObject {
        val reqJson = JsonObject()
            .put(DATA, data)
            .put(TYPE,type)
            .put(ACTION, action)
        try {
            return this.vertx.eventBus().request<JsonObject>(clazz.name, reqJson).await().body()
        } catch (e: Exception) {
            println("EB request data failed cause:${e.message}")
            throw RuntimeException(e)
        }
    }
}