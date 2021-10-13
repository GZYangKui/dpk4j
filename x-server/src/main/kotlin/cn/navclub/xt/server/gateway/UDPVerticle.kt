package cn.navclub.xt.server.gateway

import cn.navclub.xt.server.AbstractEBVerticle
import cn.navclub.xtm.core.decode.RecordParser
import cn.navclub.xtm.core.encode.SocketDataEncode
import cn.navclub.xtm.core.enums.SocketCMD
import io.vertx.core.json.JsonObject
import io.vertx.core.net.SocketAddress
import io.vertx.kotlin.coroutines.await
import java.lang.Exception
import java.lang.RuntimeException

class UDPVerticle : AbstractEBVerticle() {

    private val map = mutableMapOf<Int, SocketAddress>()

    override suspend fun start() {
        val dataSocket = vertx.createDatagramSocket()
        dataSocket.handler {
            RecordParser
                .create()
                .handler { rd ->
                    val sender = it.sender()
                    map[rd.sourceAddr] = sender
                    val buffer = SocketDataEncode.restResponse(SocketCMD.HEART_BEAT, 0, JsonObject())
                    dataSocket.send(buffer, sender.port(), sender.hostAddress())
                    println("UDP client IP address:${sender.hostAddress()},port:${sender.port()}")
                }
                .handle(it.data())
        }
        val port = 9999
        try {
            dataSocket.listen(port, "0.0.0.0").await()
        } catch (e: Exception) {
            println("Startup udp server fail cause:${e.message}")
            throw RuntimeException(e)
        }
        println("Success startup udp server in port ${port}")
    }
}