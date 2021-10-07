package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.event.WinDragEvent;
import cn.navclub.xtm.kit.client.XTClient;
import cn.navclub.xtm.kit.client.XTClientListener;
import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.enums.SocketCMD;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameGrabberProxy;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameRecorderProxy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
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


        this.remoteUser.setText(XTApp.getInstance().getRobotCode().toString());
        WinDragEvent.register(getStage(), this.box);

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
        if (record.cmd() == SocketCMD.MOUSE_ACTIVE) {
            var json = record.toJson();
            var x = json.getDouble(Constants.X);
            var y = json.getDouble(Constants.Y);
            var w = json.getDouble(Constants.WIDTH);
            var h = json.getDouble(Constants.HEIGHT);

            this.updateMousePos(x, y, w, h);
        }
    }

    /**
     * 根据比例计算出真实x和y位置
     */
    private void updateMousePos(double x, double y, double w, double h) {
        var rect = Screen.getPrimary().getBounds();

        var pw = w / rect.getWidth();
        var ph = h / rect.getHeight();

        var rx = x * pw;
        var ry = y * ph;

        System.out.println("rx=" + rx + ",ry=" + ry);

        Platform.runLater(() -> robot.mouseMove(new Point2D(rx, ry)));
    }
}
