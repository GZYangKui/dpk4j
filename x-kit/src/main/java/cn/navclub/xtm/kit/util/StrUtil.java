package cn.navclub.xtm.kit.util;

import java.util.Locale;
import java.util.UUID;

public class StrUtil {
    private static final char[] SEED = new char[]{
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K',
            'M',
            'N',
            'O',
            'P',
            'Q',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
            'Z',
            '0',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9'
    };

    /**
     * 生成指定长度字符串
     */
    public static String rdStr(int len, boolean lower) {
        var sl = SEED.length;
        var i = 0;
        var chars = new char[len];
        while (i < len) {
            var index = (int) (Math.random() * sl);
            chars[i] = SEED[index];
            i++;
        }
        var str = new String(chars);
        if (lower) {
            str = str.toLowerCase(Locale.ROOT);
        }
        return str;
    }

    /**
     *
     * 生成指定后缀名文件名
     *
     */
    public static String uidFName(String format, boolean replace) {
        var uuid = UUID.randomUUID().toString();
        if (replace) {
            uuid = uuid.replace("-", "");
        }
        return String.format("%s.%s", uuid, format);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
