package cn.navclub.xt.server

import cn.navclub.xt.server.config.ACTION
import cn.navclub.xt.server.config.DATA
import cn.navclub.xt.server.enums.APPAction
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import java.lang.RuntimeException

abstract class AbstractEBVerticle : CoroutineVerticle() {

    override suspend fun start() {
        this.vertx.eventBus().consumer<JsonObject>(this::class.java.name) {
            val json = try {
                this.onMessage(it)
            } catch (e: java.lang.Exception) {
                JsonObject()
            }

            it.reply(json)
        }
    }

    protected open fun onMessage(msg: Message<JsonObject>): JsonObject {
        return JsonObject()
    }

    protected suspend fun requestEB(clazz: Class<*>, action: APPAction, data: JsonObject): JsonObject {
        val reqJson = JsonObject()
            .put(DATA, data)
            .put(ACTION, action)
        try {
            val resp = this.vertx.eventBus().request<JsonObject>(clazz.name, reqJson).await()
            return resp.body()
        } catch (e: Exception) {
            println("EB request data failed cause:${e.message}")
            throw RuntimeException(e)
        }
    }
}