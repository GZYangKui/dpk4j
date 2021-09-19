package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;

import cn.navclub.xtm.app.util.FFmpegUtil;
import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.proxy.FFmpegFrameGrabberProxy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import org.bytedeco.javacv.Frame;

import java.nio.charset.StandardCharsets;


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

    private final FFmpegFrameGrabberProxy fProxy;

    public WinMonitorController() {
        super("WinMonitorView.fxml");
        this.setStyleSheet("WinMonitorViewStyle.css");
        this.getStage().setTitle("x-terminal");
        var rect = Screen.getPrimary().getBounds();
        //初始化FFmpeg
        this.fProxy = FFmpegFrameGrabberProxy.createProxy()
                .setConsumer(this::onReceive)
                .setFilename(":1+" + 0 + "," + 0)
                .setImgWidth((int) rect.getWidth())
                .setImgHeight((int) rect.getHeight())
                .setFormat("x11grab")
                .start();
        this.canvas.addEventFilter(MouseEvent.ANY,event -> {

        });
    }

    /**
     *
     * 刷新视图
     *
     */
    private void onReceive(Frame frame) {
        var wi = FFmpegUtil.toFXImage(frame);
        Platform.runLater(() -> {
            var width = this.canvas.getWidth();
            var height = this.canvas.getHeight();
            var context = this.canvas.getGraphicsContext2D();
            context.clearRect(0, 0, width, height);
            context.drawImage(wi, 0, 0, width, height);
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
        this.fProxy.stop();
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
