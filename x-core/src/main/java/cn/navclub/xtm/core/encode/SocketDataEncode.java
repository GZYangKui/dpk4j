package cn.navclub.xtm.core.encode;

import cn.navclub.xtm.core.enums.ClientStatus;
import cn.navclub.xtm.core.enums.SocketCMD;
import cn.navclub.xtm.core.enums.TCPDirection;
import cn.navclub.xtm.core.util.ByteUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * 根据自定义定义协议方式,组装格式化数据
 *
 * @author yangkui
 */
public class SocketDataEncode {
    public static Buffer encode(SocketCMD cmd, ClientStatus clientStatus, int target, int source, TCPDirection orientation, byte[] data) {
        var temp = new byte[]{'X', 'T', cmd.getCmd(),};
        var len = 0;
        if (data != null) {
            len = data.length;
        }
        var buffer = Buffer.buffer(temp);
        //添加目标地址(平台地址为0)
        buffer.appendBytes(ByteUtil.int2byte(target));
        buffer.appendBytes(ByteUtil.int2byte(source));
        //添加消息状态
        buffer.appendBytes(ByteUtil.int2byte(clientStatus.getStatus()));
        //数据长度
        buffer.appendBytes(ByteUtil.int2byte(len));
        //添加消息方向
        buffer.appendByte(orientation.getOrientation());
        if (len > 0) {
            buffer.appendBytes(data);
        }
        return buffer;
    }

    public static Buffer restRequest(SocketCMD cmd, int target, JsonObject json) {
        return rest(cmd, target, json, ClientStatus.OK, TCPDirection.REQUEST);
    }

    public static Buffer restResponse(SocketCMD cmd, int target, JsonObject json) {
        return rest(cmd, target, json, ClientStatus.OK, TCPDirection.RESPONSE);
    }

    public static Buffer restRequest(SocketCMD cmd, ClientStatus status, int target, JsonObject json) {
        return rest(cmd, target, json, status, TCPDirection.REQUEST);
    }

    public static Buffer restResponse(SocketCMD cmd, ClientStatus status, int target, JsonObject json) {
        return rest(cmd, target, json, status, TCPDirection.RESPONSE);
    }

    private static Buffer rest(SocketCMD cmd, int target, JsonObject json, ClientStatus status, TCPDirection direction) {
        byte[] arr = null;
        if (json != null) {
            arr = json.toBuffer().getBytes();
        }
        return encode(cmd, status, target, 0, direction, arr);
    }
}
