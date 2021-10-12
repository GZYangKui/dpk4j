package cn.navclub.xt.server.enums;

public enum TCPDirection {
    /**
     * 请求
     */
    REQUEST((byte) 0),
    /**
     * 响应
     */
    RESPONSE((byte) 1);

    private final byte orientation;

    TCPDirection(byte orientation) {
        this.orientation = orientation;
    }

    public byte getOrientation() {
        return orientation;
    }

    public static TCPDirection getInstance(byte value){
        for (TCPDirection direction : values()) {
            if (direction.getOrientation() == value){
                return direction;
            }
        }
        throw new RuntimeException("未知TCP方向["+value+"]");
    }
}

