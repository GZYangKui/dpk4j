package cn.navclub.xtm.core.util;

import cn.navclub.xtm.core.function.FIConsumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BiConsumer;

public class IOUtil {
    /**
     * 读取单个文件
     */
    public static void readFile(File file, BiConsumer<Integer, byte[]> callback) {
        try (var inputStream = new FileInputStream(file)) {
            var len = 0;
            var buffer = new byte[1024 * 1024 * 10];
            while ((len = inputStream.read(buffer)) != -1) {
                callback.accept(len, buffer);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * 写入文件到特定路径上
     *
     */
    public static FIConsumer<Integer, byte[], Boolean> writerFile(File dir, String filename) throws IOException {
        if (!dir.isDirectory()) {
            throw new RuntimeException("Dir must as system file direction!");
        }
        var file = new File(dir.getAbsolutePath() + File.separator + filename);
        var outStream = new FileOutputStream(file);
        return (len, arr, close) -> {
            try {
                outStream.write(arr, 0, len);
                //关闭流
                if (close){
                    outStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

    }
}
