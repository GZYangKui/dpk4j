package cn.navclub.xt.server.verticle

import cn.navclub.xt.server.AbstractEBVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

/**
 *
 * 推流相关操作
 *
 */
class OBSVerticel : AbstractEBVerticle() {
    override suspend fun start() {
        super.start()
    }

    override fun onMessage(msg: Message<JsonObject>): JsonObject {
        return JsonObject().put("key","hello,world!")
    }


}