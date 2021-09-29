package cn.navclub.xtm.kit.encode;

import cn.navclub.xtm.kit.enums.SocketCMD;
import cn.navclub.xtm.kit.util.ByteUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * 根据{@link  cn.navclub.xtm.kit.decode.impl.RecordParserImpl}定义协议方式,组装格式化数据
 *
 * @author yangkui
 */
public class SocketDataEncode {
    public static Buffer encode(SocketCMD cmd, byte[] data) {
        var len = ByteUtil.int2byte(data.length);
        var template = new byte[]{
                'X',
                'T',
                cmd.getCmd(),
        };

        var buffer = Buffer.buffer(template);
        buffer.appendBytes(len);
        buffer.appendBytes(data);

        return buffer;
    }

    public static Buffer encodeJson(SocketCMD cmd, JsonObject json) {
        return encode(cmd, json.toBuffer().getBytes());
    }
}
