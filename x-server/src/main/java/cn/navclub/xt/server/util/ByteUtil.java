package cn.navclub.xt.server.util;

public class ByteUtil {
    /**
     * 将数字转换为字节数组
     */
    public static byte[] int2byte(int i) {
        return new byte[]{
                (byte) (i & 0xff),
                (byte) (i >> 8 & 0xff),
                (byte) (i >> 16 & 0xff),
                (byte) (i >> 24 & 0xff)
        };
    }

    /**
     * 字节数组转换为整形
     */
    public static int byte2int(byte[] arr) {
        return (arr[0] & 0xff)
                | ((arr[1] & 0xff) << 8)
                | ((arr[2] & 0xff) << 16)
                | ((arr[3] & 0xff) << 24);
    }

}
