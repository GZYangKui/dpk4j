package cn.navclub.xt.server.verticle

import cn.navclub.xt.server.AbstractEBVerticle
import cn.navclub.xt.server.RestAPIVerticle
import cn.navclub.xt.server.api.CommonResult
import cn.navclub.xt.server.enums.APPAction
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

/**
 *
 * 推流相关操作
 *
 */
class OBSVerticel : RestAPIVerticle() {
    override suspend fun start() {
        super.start()
    }
}