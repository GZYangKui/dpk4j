package cn.navclub.xtm.kit.decode.impl;

import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.enums.ClientStatus;
import cn.navclub.xtm.kit.enums.SocketCMD;
import cn.navclub.xtm.kit.enums.TCPDirection;
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
 *        <th>11-14</th>
 *        <th>15-18</th>
 *        <th>19</th>
 *        <th>......</th>
 *     </tr>
 *     <tr>
 *        <td>X</td>
 *        <td>T</td>
 *        <td>数据类型</td>
 *        <td>目标地址</td>
 *        <td>源地址</td>
 *        <th>消息状态</th>
 *        <td>数据长度</td>
 *        <td>消息方向{@link TCPDirection}</td>
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
                var sourceAddr = ByteUtil.byte2int(this.buffer.getBytes(7, 11));
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
                final Buffer data;
                if (len > 0) {
                    data = this.buffer.getBuffer(HEADER_LENGTH, maxLen);
                } else {
                    data = null;
                }
                var record = new Record(
                        SocketCMD.getInstance(cmd),
                        addr,
                        sourceAddr,
                        len,
                        data,
                        ClientStatus.getInstance(status),
                        TCPDirection.getInstance(direction)
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

    @java.lang.Override
    public RecordParser exceptionHandler(Handler<java.lang.Throwable> handler) {
        this.exceptHandler = handler;
        return this;
    }
}