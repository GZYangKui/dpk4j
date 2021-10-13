package cn.navclub.xtm.core.decode;

import cn.navclub.xtm.core.decode.impl.RecordParserImpl;
import cn.navclub.xtm.core.encode.SocketDataEncode;
import cn.navclub.xtm.core.enums.ClientStatus;
import cn.navclub.xtm.core.enums.SocketCMD;
import cn.navclub.xtm.core.enums.TCPDirection;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * 封装数据解码器通用接口
 *
 * @author yangkui
 */
public interface RecordParser {
    /**
     * 传入待解析数据
     */
    void handle(Buffer buffer);

    /**
     * 注册解码成功后的回调句柄
     */
    RecordParser handler(Handler<Record> handler);

    /**
     * 注册异常处理句柄
     */
    RecordParser exceptionHandler(Handler<Throwable> handler);

    /**
     * 创建默认解码器
     */
    static RecordParser create() {
        return new RecordParserImpl();
    }

    class Record {
        /**
         * 实际数据
         */
        private final Buffer data;
        /**
         * {@link SocketCMD}
         */
        private final SocketCMD cmd;
        /**
         * 客户端状态
         */
        private final ClientStatus status;
        /**
         * 数据长度
         */
        private final Integer length;
        /**
         * 目标地址
         */
        private final Integer address;
        /**
         * 原始数据
         */
        private final Buffer buffer;
        /**
         * TCP方向
         */
        private final TCPDirection direction;
        /**
         * 源地址
         */
        private final Integer sourceAddr;

        public Record(SocketCMD cmd,
                      Integer address,
                      Integer length,
                      Buffer data,
                      Buffer buffer,
                      ClientStatus status,
                      TCPDirection direction,
                      Integer sourceAddr) {
            this.cmd = cmd;
            this.address = address;
            this.length = length;
            this.data = data;
            this.buffer = buffer;
            this.status = status;
            this.direction = direction;
            this.sourceAddr = sourceAddr;
        }

        /**
         * 重组Socket消息
         */
        public Buffer rebuild(int socketId) {
            byte[] arr = null;
            if (this.data != null) {
                arr = this.data.getBytes();
            }
            return SocketDataEncode.encode(
                    getCmd(),
                    getStatus(),
                    getAddress(),
                    socketId,
                    getDirection(),
                    arr
            );
        }

        /**
         * 尝试将响应数据转换为json数据
         */
        public JsonObject toJson() {
            return this.data.toJsonObject();
        }

        public Buffer getData() {
            return data;
        }

        public SocketCMD getCmd() {
            return cmd;
        }

        public Integer getLength() {
            return length;
        }

        public Integer getAddress() {
            return address;
        }

        public Buffer getBuffer() {
            return buffer;
        }

        public ClientStatus getStatus() {
            return status;
        }

        public TCPDirection getDirection() {
            return direction;
        }

        public Integer getSourceAddr() {
            return sourceAddr;
        }
    }

}
