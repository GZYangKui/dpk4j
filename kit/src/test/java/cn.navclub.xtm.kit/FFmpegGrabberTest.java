package cn.navclub.xtm.kit;

import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameGrabberProxy;
import org.bytedeco.javacv.FFmpegFrameGrabber;

public class FFmpegGrabberTest {
    public static void main(String[] args) {
//        var grabber = new FFmpegFrameGrabber("rtmp://127.0.0.1/myapp");
//        grabber.setFormat("flv");
//
//        try {
//            grabber.start();
//            while (true){
//                var frame = grabber.grabFrame();
//                System.out.println(frame);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        var fProxy  = FFmpegFrameGrabberProxy.createGraProxy();

        fProxy
                .setCallback(frame-> System.out.println(frame))
                .setFilename(String.format(":%d+%d",1,0))
                .setImgWidth(1920)
                .setImgHeight(1080)
                .setFormat("x11grab")
                .start();

    }
}
