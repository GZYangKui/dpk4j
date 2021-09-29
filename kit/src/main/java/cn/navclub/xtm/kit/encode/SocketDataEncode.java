package cn.navclub.xtm.kit.encode;

import cn.navclub.xtm.kit.enums.ClientStatus;
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
    public static Buffer encode(SocketCMD cmd, ClientStatus clientStatus, int target, byte[] data) {
        var len = ByteUtil.int2byte(data.length);
        var address = ByteUtil.int2byte(target);
        var status = ByteUtil.int2byte(clientStatus.getStatus());
        var temp = new byte[]{
                'X',
                'T',
                cmd.getCmd(),
        };

        var buffer = Buffer.buffer(temp);
        //添加目标地址(平台地址为0)
        buffer.appendBytes(address);
        //天剑消息状态
        buffer.appendBytes(status);
        //数据长度
        buffer.appendBytes(len);
        buffer.appendBytes(data);

        return buffer;
    }

    public static Buffer encodeJson(SocketCMD cmd, ClientStatus clientStatus, int target, JsonObject json) {
        return encode(cmd, clientStatus, target, json.toBuffer().getBytes());
    }
}
