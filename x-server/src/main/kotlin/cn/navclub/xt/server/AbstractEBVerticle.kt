package cn.navclub.xt.server

import cn.navclub.xt.server.api.CommonResult
import cn.navclub.xt.server.api.RestResponse
import cn.navclub.xt.server.config.ACTION
import cn.navclub.xt.server.config.DATA
import cn.navclub.xt.server.config.TYPE
import cn.navclub.xt.server.enums.APPAction
import cn.navclub.xt.server.enums.TMessage
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.RuntimeException

abstract class AbstractEBVerticle : CoroutineVerticle() {
    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 在start中统一注册EventBus
     */
    override suspend fun start() {
        this.vertx.eventBus().consumer<JsonObject>(this::class.java.name) {
            launch {
                val rs = try {
                    val reqJson = it.body()
                    val data = reqJson.getJsonObject(DATA)
                    val type = TMessage.valueOf(reqJson.getString(TYPE))
                    val action = APPAction.valueOf(reqJson.getString(ACTION))
                    if (type == TMessage.INTERNAL) {
                        interReq(action, data)
                    } else {
                        restReq(action, data)
                    }
                } catch (e: java.lang.Exception) {
                    it.fail(RestResponse.ERROR, e.message)
                    logger.error("Event-bus response error!", e)
                    return@launch
                }

                it.reply(rs)
            }
        }
    }

    /**
     * 每个Verticle根据实际需求自定义实现该方法处理程序内部事件
     */
    protected open suspend fun interReq(action: APPAction, data: JsonObject): JsonObject {
        return JsonObject()
    }


    /**
     * 每个Verticle根据需求实现该方法处理HTTP请求
     */
    protected open suspend fun restReq(action: APPAction, data: JsonObject): CommonResult<*> {
        return RestResponse.success<Void>()
    }

    companion object {
        /**
         * 程序各个单元之间通信
         */
        suspend fun <T> requestEB(vertx:Vertx,clazz: Class<*>, action: APPAction, data: JsonObject, type: TMessage): T {
            val reqJson = JsonObject()
                .put(DATA, data)
                .put(TYPE, type)
                .put(ACTION, action)
            try {
                return vertx.eventBus().request<T>(clazz.name, reqJson).await().body()
            } catch (e: Exception) {
                println("EB request data failed cause:${e.message}")
                throw RuntimeException(e)
            }
        }
    }

}