package cn.navclub.xtm.kit.decode;

import cn.navclub.xtm.kit.decode.impl.RecordParserImpl;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

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
    RecordParser handler(Handler<Buffer> handler);

    /**
     *
     * 注册异常处理句柄
     *
     */
    RecordParser exceptionHandler(Handler<Throwable> handler);

    /**
     *
     * 创建默认解码器
     *
     */
    static RecordParser create() {
        return new RecordParserImpl();
    }

}
