package cn.navclub.xtm.kit.util;

public class ByteUtil {
    /**
     * 将数字转换为字节数组
     */
    public static byte[] int2byte(int i) {
        var arr = new byte[4];

        arr[0] = (byte) ((i >>> 24) & 0xff);
        arr[1] = (byte) ((i >>> 16) & 0xff);
        arr[2] = (byte) ((i >>> 8) & 0xff);
        arr[3] = (byte) (i & 0xff);

        return arr;
    }

    /**
     *
     * 字节数组转换为整形
     *
     */
    public static int byte2int(byte[] bytes) {
        int num = bytes[3] & 0xff;
        num |= ((bytes[2] << 8) & 0xff00);
        num |= ((bytes[1] << 16) & 0xff0000);
        num |= ((bytes[0] >> 24) & 0xff000000);
        return num;
    }

}
