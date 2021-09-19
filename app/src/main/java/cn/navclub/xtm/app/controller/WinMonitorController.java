package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;

import cn.navclub.xtm.kit.FFmpegFrameGrabberProxy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;


import java.nio.ByteBuffer;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2BGRA;


/**
 * 远程操作主窗口
 *
 * @author yangkui
 */
public class WinMonitorController extends AbstractWindowFXMLController<BorderPane> {
    @FXML
    private HBox bBox;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Canvas canvas;

    private final FFmpegFrameGrabberProxy proxy;

    public WinMonitorController() {
        super("WinMonitorView.fxml");
        this.setStyleSheet("WinMonitorViewStyle.css");
        this.getStage().setTitle("x-terminal");
        var rect = Screen.getPrimary().getBounds();
        //初始化FFmpeg
        this.proxy = FFmpegFrameGrabberProxy.createProxy()
                .setConsumer(this::initFFmpeg)
                .setFilename(":1+" + 0 + "," + 0)
                .setImgWidth((int) rect.getWidth())
                .setImgHeight((int) rect.getHeight())
                .setFormat("x11grab")
                .start();
    }

    private final Mat javaCVMat = new Mat();

    private final OpenCVFrameConverter.ToMat javaCVConv = new OpenCVFrameConverter.ToMat();

    private final WritablePixelFormat<ByteBuffer> formatByte = PixelFormat.getByteBgraPreInstance();


    public void initFFmpeg(Frame frame) {

        var w = frame.imageWidth;
        var h = frame.imageHeight;
        var mat = javaCVConv.convert(frame);
        opencv_imgproc.cvtColor(mat, javaCVMat, COLOR_BGR2BGRA);

        var buffer = javaCVMat.createBuffer();

        var pixelBuffer = new PixelBuffer(w, h, buffer, formatByte);

        var wi = new WritableImage(pixelBuffer);

        Platform.runLater(() -> {
            var context = this.canvas.getGraphicsContext2D();
            context.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
            context.drawImage(wi, 0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        });
    }

    @Override
    public void windowSizeChange(double width, double height) {
        this.canvas.setWidth(width);
        //重新计算画布实际高度
        this.canvas.setHeight(this.realHei());
    }

    @Override
    public void onRequestClose(WindowEvent event) {
        super.onRequestClose(event);
        this.proxy.stop();
    }

    /**
     * 计算画布能够伸缩最大高度
     */
    private double realHei() {
        var parent = getParent();
        var a = parent.getHeight();
        var b = this.menuBar.getHeight();
        var c = this.bBox.getHeight();
        return a - b - c;
    }

}
