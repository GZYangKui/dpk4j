package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.event.WinDragEvent;
import cn.navclub.xtm.app.util.UIUtil;
import cn.navclub.xtm.kit.client.XTClient;
import cn.navclub.xtm.kit.client.XTClientListener;
import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.enums.MouseAction;
import cn.navclub.xtm.kit.enums.SocketCMD;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameGrabberProxy;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameRecorderProxy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.controlsfx.control.Notifications;

public class WinGrabController extends AbstractWindowFXMLController<HBox> implements XTClientListener {

    private final Robot robot;

    private final FFmpegFrameGrabberProxy fProxy;

    private final FFmpegFrameRecorderProxy fRecord;

    @FXML
    private HBox box;
    @FXML
    private Label remoteUser;

    public WinGrabController(final Integer robotId) {
        super("WinGrabView.fxml");
        this.robot = new Robot();
        this.getStage().setAlwaysOnTop(true);
        this.getStage().initStyle(StageStyle.TRANSPARENT);
        this.getStage().getScene().setFill(Color.TRANSPARENT);

        WinDragEvent.register(getStage(), this.box);
        this.remoteUser.setText(XTApp.getInstance().getRobotCode().toString());


        MainViewController.newInstance().getXtClient().addListener(this);

        this.fRecord = FFmpegFrameRecorderProxy.createProxy();
        this.fProxy = FFmpegFrameGrabberProxy.createGraProxy();

        this.asyncInit(robotId);
    }

    /**
     * 执行异步初始化
     */
    private void asyncInit(final int robotId) {
        var screenID = getScreenID();
        var rect = Screen.getPrimary().getBounds();
        var filename = String.format(
                "rtmp://%s/myapp?robot=%d",
                XTApp.getInstance().getHost(),
                robotId
        );
        //执行异步初始化FFMpeg模块
        var future = this.fRecord
                .setFilename(filename)
                .setFormat("flv")
                .setImgWidth((int) rect.getWidth())
                .setImgHeight((int) rect.getHeight())
                .asyncStart()
                .thenAccept(it ->
                        this.fProxy
                                .setCallback(this.fRecord::push)
                                .setFilename(String.format(":%d+%d", screenID, 0))
                                .setImgWidth((int) rect.getWidth())
                                .setImgHeight((int) rect.getHeight())
                                .setFormat("x11grab")
                                .asyncStart()
                );
        future.whenComplete((r, t) -> {
            if (t != null) {
                Platform.runLater(() -> {
                    Notifications.create().text("初始化失败!").showError();
                    this.triggerClose(false);
                    MainViewController.newInstance().openWindow();
                });
            }
        });
    }

    @Override
    public void onRequestClose(WindowEvent event) {
        super.onRequestClose(event);
        this.fRecord.stop();
        this.fProxy.stop();
        MainViewController.newInstance().openWindow();
    }

    @Override
    public void stageShowChange(boolean oldValue, boolean newValue) {
        if (newValue) {
            var srnSize = UIUtil.getSrnSize();
            this.getStage().setX(srnSize.getWidth() - this.box.getWidth());
            this.getStage().setY(srnSize.getHeight() - this.box.getHeight() - 10);
        }
    }

    /**
     * 临时方法 仅适合linux桌面环境
     */
    public Integer getScreenID() {
        var map = System.getenv();
        var str = map.get("DISPLAY");
        if (str.startsWith(":")) {
            str = str.substring(1);
        }
        return Integer.parseInt(str);
    }

    @Override
    public void onMessage(XTClient client, RecordParser.Record record) {
        if (record.cmd() != SocketCMD.MOUSE_ACTIVE) {
            return;
        }
        var json = record.toJson();
        var action = MouseAction.valueOf(json.getString(Constants.ACTION));
        if (action == MouseAction.MOUSE_MOVE) {
            var x = json.getDouble(Constants.X);
            var y = json.getDouble(Constants.Y);
            Platform.runLater(() -> robot.mouseMove(new Point2D(x, y)));
        }
        if (action == MouseAction.MOUSE_PRESSED || action == MouseAction.MOUSE_RELEASED) {
            var str = json.getString(Constants.MOUSE_BTN);
            var btn = MouseButton.valueOf(str);
            Platform.runLater(() -> {
                if (action == MouseAction.MOUSE_PRESSED) {
                    robot.mousePress(btn);
                } else {
                    robot.mouseRelease(btn);
                }
            });
        }
    }
}
