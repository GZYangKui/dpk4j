package cn.navclub.xt.server

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.runBlocking
import java.lang.Exception


fun main(): Unit = runBlocking {
    val vertx = Vertx.vertx()
    try {
        vertx.deployVerticle("kt:cn.navclub.xt.server.gateway.TCPVerticel").await()
        vertx.deployVerticle("kt:cn.navclub.xt.server.gateway.WebVerticel").await()
        vertx.deployVerticle("kt:cn.navclub.xt.server.gateway.UDPVerticle").await()
        vertx.deployVerticle("kt:cn.navclub.xt.server.verticle.OBSVerticel").await()
    } catch (e: Exception) {
        println("Vertx application startup fail cause:${e.message}")
        vertx.close().await()
    }
}