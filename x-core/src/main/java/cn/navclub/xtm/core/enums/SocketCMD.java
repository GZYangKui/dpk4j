package cn.navclub.xtm.core.enums;

/**
 * 封装socket指令集
 */
public enum SocketCMD {
    UNKNOWN((byte) -1, "未知指令"),
    HEART_BEAT((byte) 0, "客户端心跳"),
    UPDATE_CLIENT_CODE((byte) 1, "更新客户端识别码"),
    REQUEST_REMOTE((byte) 2, "请求远程控制"),
    MOUSE_ACTIVE((byte) 3, "鼠标动作"),
    KEY_ACTIVE((byte) 4, "键盘动作");

    private final byte cmd;
    private final String message;

    SocketCMD(byte cmd, String message) {
        this.cmd = cmd;
        this.message = message;
    }

    public byte getCmd() {
        return cmd;
    }

    public String getMessage() {
        return message;
    }

    public static SocketCMD getInstance(int cmd) {
        for (SocketCMD value : values()) {
            if (value.cmd == cmd) {
                return value;
            }
        }
        return SocketCMD.UNKNOWN;
    }
}
