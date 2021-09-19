package cn.navclub.xtm.kit.decode.impl;

import cn.navclub.xtm.kit.decode.RecordParser;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

/**
 * 默认数据编码格式如下:
 *
 * <table border="1">
 *     <tr>
 *        <th>0</th>
 *        <th>1</th>
 *        <th>2</th>
 *        <th>3-6</th>
 *        <th>......</th>
 *     </tr>
 *     <tr>
 *        <td>X</td>
 *        <td>T</td>
 *        <td>数据类型</td>
 *        <td>数据长度</td>
 *        <td>真实数据</td>
 *     </tr>
 * </table>
 *
 * @author yangkui
 */
public class RecordParserImpl implements RecordParser {
    private Handler<Buffer> handler;
    private Handler<Throwable> exceptHandler;

    @Override
    public void handle(Buffer buffer) {

    }

    @Override
    public RecordParser handler(Handler<Buffer> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public RecordParser exceptionHandler(Handler<Throwable> handler) {
        this.exceptHandler = handler;
        return this;
    }
}
