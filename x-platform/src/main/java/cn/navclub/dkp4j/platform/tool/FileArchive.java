package cn.navclub.dkp4j.platform.tool;


import java.io.File;

/**
 *
 * @deprecated 由于lzo压缩算法压缩率不理想,暂时弃用该压缩算法，改用7z算法
 *
 */
@Deprecated
public class FileArchive {
    static {
        System.loadLibrary("jnidkp4j");
    }

    /**
     * 压缩src文件到dest文件
     */
    public static native int fileEncode(File src, File dest);

    /**
     * 解压src文件到dest文件
     */
    public static native int fileDecode(File src, File dest);

    /**
     * 压缩字节数组
     */
    public static native byte[] byteEncode(byte[] src);

    /**
     * 解压字节数组
     */
    public static native byte[] byteDecode(byte[] src);
}