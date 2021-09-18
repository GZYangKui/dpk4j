package cn.navclub.xtm.app;

import cn.navclub.xtm.app.controller.WinMonitorController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.ffmpeg.global.avutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * XTerminal程序启动类
 *
 *
 * @author yangkui
 *
 */
public class XTM extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(XTM.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        new WinMonitorController().openWindow();
//       var grabbar =  new FFmpegFrameGrabber("Desktop");
//        System.out.println(avutil.class);
//        grabbar.start();
        FFmpegFrameGrabber.tryLoad();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
