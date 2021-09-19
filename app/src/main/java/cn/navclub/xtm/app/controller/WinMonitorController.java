package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;


import java.nio.ByteBuffer;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2BGRA;

import static org.opencv.imgproc.Imgproc.CONTOURS_MATCH_I1;
import static org.opencv.imgproc.Imgproc.cvtColor;


/**
 * 远程操作主窗口
 *
 * @author yangkui
 */
public class WinMonitorController extends AbstractWindowFXMLController<BorderPane> {

    @FXML
    private Canvas canvas;


    public WinMonitorController() {
        super("WinMonitorView.fxml");
        this.getStage().setTitle("x-terminal");
        new Thread(this::initFFmpeg).start();
    }

    private final Mat javaCVMat = new Mat();

    private final OpenCVFrameConverter.ToMat javaCVConv = new OpenCVFrameConverter.ToMat();

    private final WritablePixelFormat<ByteBuffer> formatByte = PixelFormat.getByteBgraPreInstance();


    public void initFFmpeg() {
        var screen = Screen.getPrimary();
        var grabber = new FFmpegFrameGrabber(":1+" + 0 + "," + 0);

        grabber.setFormat("x11grab");
        grabber.setImageWidth((int) screen.getBounds().getWidth());
        grabber.setImageHeight((int) screen.getBounds().getHeight());

        try {
            grabber.start();
            while (true) {
                var frame = grabber.grabFrame();
                var w = frame.imageWidth;
                var h = frame.imageHeight;
                var mat = javaCVConv.convert(frame);
                opencv_imgproc.cvtColor(mat, javaCVMat, COLOR_BGR2BGRA);

                var buffer = javaCVMat.createBuffer();

                var pixelBuffer = new PixelBuffer(w,h,buffer,formatByte);

                var wi = new WritableImage(pixelBuffer);


                Platform.runLater(()->{
                    var context = this.canvas.getGraphicsContext2D();
                    context.clearRect(0,0,this.canvas.getWidth(),this.canvas.getHeight());
                    context.drawImage(wi,this.canvas.getWidth(),this.canvas.getHeight());
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
