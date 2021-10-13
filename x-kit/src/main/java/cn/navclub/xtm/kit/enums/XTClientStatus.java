package cn.navclub.xtm.kit.enums;

public enum XTClientStatus {
    NOT_CONNECT("未连接"),
    CONNECTING("连接中"),
    CONNECTED("连接成功"),
    BROKEN_CONNECT("连接断开");

    private final String message;

    XTClientStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
