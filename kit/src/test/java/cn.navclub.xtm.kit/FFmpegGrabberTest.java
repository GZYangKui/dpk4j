package cn.navclub.xtm.kit;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class FFmpegGrabberTest {
    public static void main(String[] args) {
        var grabber = new FFmpegFrameGrabber("rtmp://127.0.0.1/myapp");
        grabber.setFormat("flv");

        try {
            grabber.start();
            while (true){
                var frame = grabber.grabFrame();
                System.out.println(frame);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
