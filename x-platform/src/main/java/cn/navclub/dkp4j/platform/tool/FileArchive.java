package cn.navclub.dkp4j.platform.tool;

import cn.navclub.dkp4j.platform.enums.ArcResult;

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

    public static void main(String[] args) {
        var file = new File("test.txt");
        var file1 = new File("test.txt.dkp4j");

        var rs = compress(file,file1);

        System.out.println(ArcResult.getInstance(rs).getMessage());
    }
}