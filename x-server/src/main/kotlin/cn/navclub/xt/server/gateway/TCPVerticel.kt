package cn.navclub.xt.server.gateway

import cn.navclub.xt.server.config.CODE
import cn.navclub.xt.server.decode.RecordParser
import cn.navclub.xt.server.encode.SocketDataEncode
import cn.navclub.xt.server.enums.ClientStatus
import cn.navclub.xt.server.enums.SocketCMD
import cn.navclub.xt.server.enums.TCPDirection
import io.vertx.core.json.JsonObject
import io.vertx.core.net.NetServerOptions
import io.vertx.core.net.NetSocket
import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.math.BigDecimal
import java.util.*

class TCPVerticel : CoroutineVerticle() {
    private val map = HashMap<Int, NetSocket>()

    override suspend fun start() {
        val port = 8888

        val options = NetServerOptions()
        options.idleTimeout = 300

        vertx.createNetServer(options).connectHandler(this::init).listen(port) {
            if (it.failed()) {
                println("Net server startup failed cause:${it.cause().message}")
            } else {
                println("Net server success listener in port $port")
            }
        }
    }

    private fun init(socket: NetSocket) {
        val socketId = rdSocketId()
        map[socketId] = socket
        val parser = RecordParser
            .create()
            .handler {
                handleOnMsg(socketId, it)
            }
        socket.handler {
            parser.handle(it)
        }
        socket.closeHandler {
            this.map.remove(socketId)
        }
        socket.exceptionHandler { t ->
            socket.close()
            this.map.remove(socketId)
            t.printStackTrace()
        }
        val buffer =
            SocketDataEncode.restResponse(SocketCMD.UPDATE_CLIENT_CODE, socketId, JsonObject().put(CODE, socketId))
        socket.write(buffer)
    }

    private fun handleOnMsg(socketId: Int, record: RecordParser.Record) {
        val address = record.address
        //平台信息
        if (address == 0) {
            this.platCmd(socketId,record)
            return
        }
        val client = map[address]
        //在线转发
        if (client != null) {
            client.write(record.rebuild(socketId))
        } else {
            //响应客户端离线状态
            val source = this.map[socketId] ?: return
            source.write(
                SocketDataEncode.encode(
                    record.cmd,
                    ClientStatus.OFFLINE,
                    address,
                    0,
                    TCPDirection.RESPONSE,
                    ByteArray(0)
                )
            )
        }
    }

    private fun platCmd(socketId: Int,record: RecordParser.Record){
        //响应心跳包
        if (record.cmd == SocketCMD.HEART_BEAT) {
            val buffer = SocketDataEncode.restResponse(
                SocketCMD.HEART_BEAT,
                socketId,
                null
            )
            this.map[socketId]?.write(buffer)
        }
    }

    /**
     * 对每个连接生成随机验证码
     */
    private fun rdSocketId(): Int {
        val base = 1e8
        val span = 9e8
        val num = base + (Math.random() * span).toInt()
        val str = BigDecimal(num).toBigInteger().toInt()
        return if (map.containsKey(str)) {
            this.rdSocketId()
        } else {
            str
        }
    }
}