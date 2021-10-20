package cn.navclub.xt.server.verticle

import cn.navclub.xt.server.RestAPIVerticle
import cn.navclub.xt.server.api.CommonResult
import cn.navclub.xt.server.api.RestResponse
import cn.navclub.xt.server.enums.APPAction
import io.vertx.core.json.JsonObject

/**
 *
 * 用户相关操作接口
 *
 */
class SSOVerticel : RestAPIVerticle() {

    override suspend fun start() {
        super.start()
    }

    override fun restReq(action: APPAction, data: JsonObject): CommonResult<Any> {
        if (action == APPAction.USER_LOGIN){
            return RestResponse.success()
        }
        return RestResponse.fail("404")
    }
}