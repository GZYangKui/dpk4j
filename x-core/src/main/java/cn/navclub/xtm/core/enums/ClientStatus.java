package cn.navclub.xtm.core.enums;

public enum ClientStatus {
    OK(200, "操作成功"),
    UNKNOWN(-1,"未知状态"),
    CLIENT_BUSY(1000, "占线中"),
    OFFLINE(1001, "客户端离线"),
    UNAUTHORIZED(1002,"远程口令错误");

    private final int status;
    private final String message;

    ClientStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static ClientStatus getInstance(int status){
        for (ClientStatus value : values()) {
            if (value.status==status){
                return value;
            }
        }
        return UNKNOWN;
    }
}
