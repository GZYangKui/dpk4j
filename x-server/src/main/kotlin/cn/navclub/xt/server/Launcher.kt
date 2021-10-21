package cn.navclub.xt.server

import cn.navclub.xt.server.api.CommonResult
import cn.navclub.xt.server.codgen.RestRespCodegen
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.runBlocking
import java.lang.Exception


fun main(): Unit = runBlocking {
    val vertx = Vertx.vertx()
    try {
        val resultCodegen = RestRespCodegen()

        vertx.eventBus().registerDefaultCodec(CommonResult::class.java, resultCodegen)
        vertx.deployVerticle("kt:cn.navclub.xt.server.gateway.TCPVerticel").await()
        vertx.deployVerticle("kt:cn.navclub.xt.server.gateway.WebVerticel").await()
        vertx.deployVerticle("kt:cn.navclub.xt.server.gateway.UDPVerticle").await()
        vertx.deployVerticle("kt:cn.navclub.xt.server.verticle.OBSVerticel").await()
        vertx.deployVerticle("kt:cn.navclub.xt.server.verticle.SSOVerticel").await()

    } catch (e: Exception) {
        e.printStackTrace()
        println("Vertx application startup fail cause:${e.message}")
        vertx.close().await()
    }
}