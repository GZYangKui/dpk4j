package cn.navclub.dkp4j.platform.tool;


import java.io.File;

public class FileArchive {
    static {
        System.loadLibrary("jnidkp4j");
    }
    /**
     * 压缩src文件到dest文件
     */
    public static native int compress(File src, File dest);

    /**
     * 解压src文件到dest文件
     */
    public static native int decompress(File src, File dest);
}