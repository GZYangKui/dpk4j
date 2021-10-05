package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;

import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.util.FFmpegUtil;
import cn.navclub.xtm.app.util.UIUtil;
import cn.navclub.xtm.kit.encode.SocketDataEncode;
import cn.navclub.xtm.kit.enums.SocketCMD;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameGrabberProxy;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameRecorderProxy;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.bytedeco.javacv.Frame;
import org.controlsfx.control.Notifications;


/**
 * 远程操作主窗口
 *
 * @author yangkui
 */
public class WinMonitorController extends AbstractWindowFXMLController<BorderPane> {
    @FXML
    private HBox bBox;
    @FXML
    private Canvas canvas;

    private final FFmpegFrameGrabberProxy fProxy;

    private final double width;
    private final double height;


    public WinMonitorController(final Integer robotId, final double width, double height) {
        super("WinMonitorView.fxml");

        this.width = width;
        this.height = height;
        //初始化窗口大小
        final double w, h;
        var rect = UIUtil.getSrnSize();
        if (width <= rect.getWidth() && height <= rect.getHeight()) {
            w = width;
            h = height;
        } else {
            w = rect.getWidth();
            h = rect.getHeight();
        }
        this.getStage().setWidth(w);
        this.getStage().setHeight(h);
        this.getStage().setResizable(false);
        this.getStage().setTitle("x-terminal");

        this.canvas.addEventFilter(MouseEvent.ANY, this::filterMouseEvent);

        //初始化FFMpeg
        this.fProxy = FFmpegFrameGrabberProxy.createGraProxy();
        this.asyncInit(robotId);
    }

    /**
     * 异步初始化{@link FFmpegFrameGrabberProxy} 代理类
     */
    private void asyncInit(int robotId) {

        var filename = String.format(
                "rtmp://%s/myapp?robotId=%d",
                XTApp.getInstance().getHost(),
                robotId
        );

        var future = this.fProxy
                .setCallback(this::onReceive)
                .setFilename(filename)
                .setImgWidth((int) width)
                .setImgHeight((int) height)
                .setFormat("flv")
                .asyncStart();

        future.whenComplete((r, t) -> {
            if (t != null) {
                Platform.runLater(() -> {
                    Notifications.create().text("获取数据流失败!").showError();
                    this.triggerClose(false);
                    MainViewController.newInstance().openWindow();
                });
            }
        });
    }

    /**
     * 捕获画布上的鼠标事件并转发至被控制终端
     */
    private void filterMouseEvent(MouseEvent event) {

    }


    /**
     * 刷新视图
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
        MainViewController.newInstance().openWindow();
    }

    /**
     * 计算画布能够伸缩最大高度
     */
    private double realHei() {
        var parent = getParent();
        var a = parent.getHeight();
        var c = this.bBox.getHeight();
        return a - c;
    }

}
