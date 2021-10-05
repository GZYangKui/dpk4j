package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;

import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.util.FFmpegUtil;
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


    public WinMonitorController(final Integer robotId) {
        super("WinMonitorView.fxml");
        this.getStage().setTitle("x-terminal");
        var rect = Screen.getPrimary().getBounds();

        //初始化FFmpeg
        this.fProxy = FFmpegFrameGrabberProxy.createGraProxy();

        var future = this.fProxy
                .setCallback(this::onReceive)
                .setFilename(String.format("rtmp://%s/myapp?robotId=%d", XTApp.getInstance().getHost(), robotId))
                .setImgWidth((int) rect.getWidth())
                .setImgHeight((int) rect.getHeight())
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

        this.canvas.addEventFilter(MouseEvent.ANY, event -> {
            var et = event.getEventType();
            if (et == MouseEvent.MOUSE_MOVED) {
                var x = event.getSceneX();
                var y = event.getSceneY();
                //获取当前场景图高度和宽度
                var height = this.realHei();
                var width = this.getStage().getWidth();

                var json = new JsonObject();

                json.put(Constants.X, x);
                json.put(Constants.Y, y);

                json.put(Constants.WIDTH, width);
                json.put(Constants.HEIGHT, height);

                var buffer = SocketDataEncode.restRequest(
                        SocketCMD.MOUSE_ACTIVE,
                        XTApp.getInstance().getRemoteCode(),
                        json
                );

                MainViewController.newInstance().getXtClient().send(buffer);
            }
        });
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
