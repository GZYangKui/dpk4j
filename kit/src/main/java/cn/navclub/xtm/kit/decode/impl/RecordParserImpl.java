package cn.navclub.xtm.kit.decode.impl;

import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.enums.ClientStatus;
import cn.navclub.xtm.kit.enums.SocketCMD;
import cn.navclub.xtm.kit.util.ByteUtil;
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
 *        <th>7-10</th>
 *        <th>10-13</th>
 *        <th>......</th>
 *     </tr>
 *     <tr>
 *        <td>X</td>
 *        <td>T</td>
 *        <td>数据类型</td>
 *        <td>目标地址</td>
 *        <th>消息状态</th>
 *        <td>数据长度</td>
 *        <td>真实数据</td>
 *     </tr>
 * </table>
 * <p>
 * 关于数据类型,请查看 {@link SocketCMD}
 *
 * @author yangkui
 */
public class RecordParserImpl implements RecordParser {
    /**
     * 数据包头长度
     */
    private static final int HEADER_LENGTH = 15;


    private Handler<Record> handler;
    private Handler<Throwable> exceptHandler = t -> {
    };

    private volatile boolean parsing;
    private Buffer buffer = Buffer.buffer();

    @Override
    public void handle(Buffer buffer) {
        this.buffer.appendBuffer(buffer);
        try {
            this.handleParser();
        } catch (Exception e) {
            e.printStackTrace();
            this.exceptHandler.handle(e);
        }
    }

    private void handleParser() {
        if (parsing) {
            return;
        }
        parsing = true;
        var i = 0;
        try {
            do {
                var arr = this.buffer.getBytes();
                //数据头长度不够=>不处理
                if (arr.length < HEADER_LENGTH) {
                    return;
                }
                var a = arr[i];
                var b = arr[i + 1];
                //未检测到标志信息
                if (!(a == 'X' && b == 'T')) {
                    return;
                }
                var cmd = arr[i + 2];
                var addr = ByteUtil.byte2int(this.buffer.getBytes(3, 7));
                var status = ByteUtil.byte2int(this.buffer.getBytes(7, 11));
                var len = ByteUtil.byte2int(this.buffer.getBytes(11, 15));
                var maxLen = HEADER_LENGTH + len;
                //数据长度不够=>不做处理
                if (maxLen > this.buffer.length()) {
                    return;
                }
                var data = this.buffer.getBuffer(HEADER_LENGTH, maxLen);
                var record = new Record(
                        SocketCMD.getInstance(cmd),
                        addr,
                        len,
                        data,
                        ClientStatus.getInstance(status)
                );
                this.handler.handle(record);
                this.buffer = this.buffer.getBuffer(maxLen, this.buffer.length());
            } while (true);
        } finally {
            parsing = false;
        }
    }

    @Override
    public RecordParser handler(Handler<Record> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public RecordParser exceptionHandler(Handler<Throwable> handler) {
        this.exceptHandler = handler;
        return this;
    }
}