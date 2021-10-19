package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.event.WinDragEvent;
import cn.navclub.xtm.app.util.UIUtil;
import cn.navclub.xtm.core.decode.RecordParser;
import cn.navclub.xtm.core.enums.SocketCMD;
import cn.navclub.xtm.kit.XLHelper;
import cn.navclub.xtm.kit.client.XClient;
import cn.navclub.xtm.kit.client.impl.TCPClient;
import cn.navclub.xtm.kit.enums.KeyEventAction;
import cn.navclub.xtm.kit.enums.MouseEventAction;
import cn.navclub.xtm.kit.listener.XTClientListener;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameGrabberProxy;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameRecorderProxy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.bytedeco.ffmpeg.global.avcodec;
import org.controlsfx.control.Notifications;

import java.util.List;

public class WinGrabController extends AbstractWindowFXMLController<HBox> implements XTClientListener {

    private final Robot robot;

    private final FFmpegFrameGrabberProxy fProxy;

    private final FFmpegFrameRecorderProxy fRecord;

    @FXML
    private HBox box;
    @FXML
    private Label remoteUser;

    public WinGrabController() {
        super("WinGrabView.fxml");
        this.robot = new Robot();
        this.getStage().setAlwaysOnTop(true);
        this.getStage().initStyle(StageStyle.TRANSPARENT);
        this.getStage().getScene().setFill(Color.TRANSPARENT);

        WinDragEvent.register(getStage(), this.box);
        this.remoteUser.setText(XTApp.getInstance().getRobotCode().toString());

        XLHelper.addListener(this);

        this.fRecord = FFmpegFrameRecorderProxy.createProxy();
        this.fProxy = FFmpegFrameGrabberProxy.createGraProxy();

        this.asyncInit();
    }

    /**
     * 执行异步初始化
     */
    private void asyncInit() {
        var app = XTApp.getInstance();
        var screenID = getScreenID();
        var rect = Screen.getPrimary().getBounds();
        var filename = String.format(
                "rtmp://%s/myapp/%d",
                app.getHost(),
                app.getRobotCode()
        );
        //执行异步初始化FFMpeg模块
        var future = this.fRecord
                .setFilename(filename)
                .setFormat("flv")
                .setFrameNumber(50)
                .setVideoCodec(avcodec.AV_CODEC_ID_H264)
                .setImgWidth((int) rect.getWidth())
                .setFrameRate(60)
                .setImgHeight((int) rect.getHeight())
                .asyncStart()
                .thenAccept(it ->
                        this.fProxy
                                .setCallback(this.fRecord::push)
                                .setFrameNumber(50)
                                .setFrameRate(60)
                                .addOption("draw_mouse", "0")
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
        XLHelper.removeListener(this);
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
    public void onMessage(boolean udp, XClient client, RecordParser.Record record) {
        if (record.getCmd() == SocketCMD.MOUSE_ACTIVE) {
            this.mouseEvent(record);
        }
        if (record.getCmd() == SocketCMD.KEY_ACTIVE) {
            this.keyEvent(record);
        }
    }

    private static final List<SocketCMD> ACTIONS = List.of(
            SocketCMD.MOUSE_ACTIVE,
            SocketCMD.KEY_ACTIVE
    );

    @Override
    public List<SocketCMD> actions() {
        return ACTIONS;
    }

    private void mouseEvent(RecordParser.Record record) {
        var json = record.toJson();
        var action = MouseEventAction.valueOf(json.getString(Constants.ACTION));
        if (action == MouseEventAction.MOUSE_MOVE) {
            var x = json.getDouble(Constants.X);
            var y = json.getDouble(Constants.Y);
            Platform.runLater(() -> robot.mouseMove(new Point2D(x, y)));
        }
        if (action == MouseEventAction.MOUSE_PRESSED || action == MouseEventAction.MOUSE_RELEASED) {
            var str = json.getString(Constants.MOUSE_BTN);
            var btn = MouseButton.valueOf(str);
            Platform.runLater(() -> {
                if (action == MouseEventAction.MOUSE_PRESSED) {
                    robot.mousePress(btn);
                } else {
                    robot.mouseRelease(btn);
                }
            });
        }
        if (action == MouseEventAction.MOUSE_DRAGGED) {
            var x = json.getDouble(Constants.X);
            var y = json.getDouble(Constants.Y);
            Platform.runLater(() -> {
                //先按下
                this.robot.mousePress(MouseButton.PRIMARY);
                //再移动
                this.robot.mouseMove(new Point2D(x, y));
            });
        }
    }

    private void keyEvent(RecordParser.Record record) {
        var json = record.toJson();
        var keyCode = KeyCode.valueOf(json.getString(Constants.KEY_CODE));
        var action = KeyEventAction.valueOf(json.getString(Constants.ACTION));
        Platform.runLater(() -> {
            if (action == KeyEventAction.KEY_PRESSED) {
                this.robot.keyPress(keyCode);
            } else {
                this.robot.keyRelease(keyCode);
            }
        });
    }

    @FXML
    private void stop() {
        this.triggerClose(false);
    }
}
