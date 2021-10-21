package cn.navclub.xt.server.verticle

import cn.navclub.xt.server.AbstractEBVerticle
import cn.navclub.xt.server.api.CommonResult
import cn.navclub.xt.server.api.RestResponse
import cn.navclub.xt.server.enums.APPAction
import cn.navclub.xt.server.service.UserService
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.await

/**
 *
 * 用户相关操作接口
 *
 */
class SSOVerticel : AbstractEBVerticle() {
    private lateinit var userService:UserService

    override suspend fun start() {
        super.start()
        this.userService = UserService.create(vertx)
    }

    override suspend fun restReq(action: APPAction, data: JsonObject): CommonResult<*> {
        if (action == APPAction.USER_LOGIN){
            val rs =  userService.login().await()

            return rs
        }
        return RestResponse.fail<Void>("404")
    }
}