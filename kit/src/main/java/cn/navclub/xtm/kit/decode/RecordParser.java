package cn.navclub.xtm.kit.decode;

import cn.navclub.xtm.kit.decode.impl.RecordParserImpl;
import cn.navclub.xtm.kit.enums.SocketCMD;
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

    record Record(SocketCMD cmd, Integer address, Integer length, Buffer data) {
        /**
         *
         * 将接收到的数据转换为json格式
         *
         */
        public JsonObject toJson(){
            return data().toJsonObject();
        }
    }


}

