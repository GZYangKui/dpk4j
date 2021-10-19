package cn.navclub.dpk4j.platform;

import cn.navclub.dkp4j.platform.tool.FileArchive;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Test {
    public static void main(String[] args) {

//        var arr = new byte[1024*1024*10];
//
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = ((byte) (-128 + Math.random() * 255));
//        }

//        System.out.println("原始数组:"+Arrays.toString(arr));
        try {
            var fileInputStream = new FileInputStream("./test.txt");
            //1G
            byte[] buffer = new byte[1024 * 1024];
            var len = 0;
            var compressSize = 0;
            var timestamp = System.currentTimeMillis();
            while ((len = fileInputStream.read(buffer)) != -1) {
                var temp = new byte[len];
                System.arraycopy(buffer, 0, temp, 0, len);

                var out = FileArchive.byteEncode(temp);

                var out1 = FileArchive.byteDecode(out);

//                new FileOutputStream("tttt.txt").write(out1);
            }

            System.out.println(compressSize);

//        System.out.println("压缩后的数组:"+Arrays.toString(out));

//            var out1 = FileArchive.byteDecode(out);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        System.out.println("解压后的数组:" + Arrays.toString(out1));

    }
}
