package cn.navclub.dkp4j.platform.enums;

public enum ArcResult {
    OK(0, "压缩/解压成功"),
    NULL_POINTER(-(2 >>> 1), "输入/输出不能为空"),
    INPUT_NOT_FOUND(-(4 >>> 1), "输入文件不存在"),
    IO_HAPPENED_EXCEPT(-(6 >>> 1), "读取/写入文件失败"),
    CD_COM_HAPPENED_EXCEPT(-(8 >>> 1), "压缩/解压过程发生错误");

    private final int code;
    private final String message;

    ArcResult(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ArcResult getInstance(int value) {
        for (ArcResult arcResult : values()) {
            if (arcResult.code == value) {
                return arcResult;
            }
        }
        throw new RuntimeException("未知压缩结果");
    }
}
