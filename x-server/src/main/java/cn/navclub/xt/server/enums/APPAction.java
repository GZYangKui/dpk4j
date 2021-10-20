package cn.navclub.xt.server.enums;

public enum APPAction {
    UNKNOWN("未知action"),
    USER_LOGIN("用户登录"),
    REQUEST_PUBLISH("请求推流");

    private final String message;

    APPAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
