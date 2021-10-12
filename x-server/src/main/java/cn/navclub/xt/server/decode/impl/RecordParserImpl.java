package cn.navclub.xt.server.decode.impl;

import cn.navclub.xt.server.decode.RecordParser;
import cn.navclub.xt.server.enums.ClientStatus;
import cn.navclub.xt.server.enums.SocketCMD;
import cn.navclub.xt.server.enums.TCPDirection;
import cn.navclub.xt.server.util.ByteUtil;
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
 * 关于数据类型,请查看 {@link cn.navclub.xt.server.enums.SocketCMD}
 *
 * @author yangkui
 */
public class RecordParserImpl implements RecordParser {
    /**
     * 数据包头长度
     */
    private static final int HEADER_LENGTH = 20;


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

        try {
            do {
                var i = 0;
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
                //目标地址
                var addr = ByteUtil.byte2int(this.buffer.getBytes(3, 7));
                //源地址
                var source = ByteUtil.byte2int(this.buffer.getBytes(7, 11));
                //消息状态
                var status = ByteUtil.byte2int(this.buffer.getBytes(11, 15));
                //消息长度
                var len = ByteUtil.byte2int(this.buffer.getBytes(15, 19));
                //消息方向
                var direction = this.buffer.getByte(19);

                var maxLen = HEADER_LENGTH + len;
                //数据长度不够=>不做处理
                if (maxLen > this.buffer.length()) {
                    return;
                }
                var buffer = this.buffer.getBuffer(0, maxLen);
                final Buffer data;
                if (len > 0) {
                    data = this.buffer.getBuffer(HEADER_LENGTH, maxLen);
                } else {
                    data = null;
                }
                var record = new Record(
                        SocketCMD.getInstance(cmd),
                        addr,
                        len,
                        data,
                        buffer,
                        ClientStatus.getInstance(status),
                        TCPDirection.getInstance(direction),
                        source
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
