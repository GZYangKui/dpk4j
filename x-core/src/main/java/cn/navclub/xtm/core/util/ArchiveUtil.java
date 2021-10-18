package cn.navclub.xtm.core.util;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.File;
import java.io.IOException;

public class ArchiveUtil {
    /**
     * 将多个文件压缩为一个7z压缩包
     */
    public static void archive7Z(File dst, File... files) {
        try (var outputFile = new SevenZOutputFile(dst)) {
            for (File file : files) {
                var filename = file.getName();
                if (!file.exists()) {
                    throw new RuntimeException("待压缩文件:" + filename + "不存在!");
                }
                var entry = outputFile.createArchiveEntry(file, filename);
                outputFile.putArchiveEntry(entry);
                IOUtil.readFile(file, (len, buf) -> {
                    try {
                        outputFile.write(buf, 0, len);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            outputFile.closeArchiveEntry();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将7Z压缩文件解压到指定目录
     */
    public static void unArchive7Z(File dir, File src) {
        if (!src.exists()) {
            throw new RuntimeException("待解压文件不存在!");
        }
        if (!dir.isDirectory()) {
            throw new RuntimeException("解压目的地必须为文件目录!");
        }
        //10MB
        var buf = new byte[1024 * 1024 * 10];
        try (var zFile = new SevenZFile(src)) {
            SevenZArchiveEntry entry;
            while ((entry = zFile.getNextEntry()) != null) {
                var fn = entry.getName();
                var len = -1;
                var count = 0;
                var consumer = IOUtil.writerFile(dir, fn);
                while ((len = zFile.read(buf)) != -1) {
                    count += len;
                    consumer.accept(len, buf, count >= entry.getSize());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
