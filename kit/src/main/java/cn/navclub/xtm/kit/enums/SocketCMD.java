package cn.navclub.xtm.kit.enums;

/**
 * 封装socket指令集
 */
public enum SocketCMD {
    UNKNOWN_CMD((byte) -1, "未知指令"),
    HEART_BEAT((byte) 0, "客户端心跳"),
    UPDATE_CLIENT_CODE((byte) 1, "更新客户端识别码");

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
}