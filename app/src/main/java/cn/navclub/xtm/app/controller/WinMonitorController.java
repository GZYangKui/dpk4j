package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.AssetsHelper;
import cn.navclub.xtm.app.base.AbstractWindowFXMLController;

import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.event.WinDragEvent;
import cn.navclub.xtm.app.util.FFmpegUtil;
import cn.navclub.xtm.app.util.UIUtil;
import cn.navclub.xtm.kit.encode.SocketDataEncode;
import cn.navclub.xtm.kit.enums.MouseAction;
import cn.navclub.xtm.kit.enums.SocketCMD;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameGrabberProxy;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.bytedeco.javacv.Frame;
import org.controlsfx.control.Notifications;

/**
 * 远程操作主窗口
 *
 * @author yangkui
 */
public class WinMonitorController extends AbstractWindowFXMLController<BorderPane> {
    @FXML
    private Button wsBtn;
    @FXML
    private HBox topBox;
    @FXML
    private Canvas canvas;
    @FXML
    private VBox contentBox;
    @FXML
    private Label robotInfo;
    @FXML
    private ScrollPane scrollPane;


    /**
     * 判断当前窗口是否最大化
     */
    private boolean maxWin;


    private final double width;
    private final double height;
    private final double wScala = .9;
    private final FFmpegFrameGrabberProxy fProxy;

    private static final Image MAX_ICON = AssetsHelper.loadImg("win/max@1x.png");
    private static final Image MAX_ACTIVE_ICON = AssetsHelper.loadImg("win/max_active@1x.png");

    public WinMonitorController(final Integer robotId, final double width, double height) {
        super("WinMonitorView.fxml");
        this.width = width;
        this.height = height;

        this.robotInfo.setText("远程控制 " + robotId);

        WinDragEvent.register(getStage(), this.topBox);

        this.canvas.setWidth(width);
        this.canvas.setHeight(height);

        this.canvas.addEventFilter(MouseEvent.ANY, this::filterMouseEvent);


        var rect = UIUtil.getSrnSize();


        //设置当前窗口大小
        this.getStage().setResizable(false);
        this.getStage().initStyle(StageStyle.UNDECORATED);
        this.getStage().setWidth(rect.getWidth() * wScala);
        this.getStage().setHeight(rect.getHeight() * wScala);

        //初始化FFMpeg
        this.fProxy = FFmpegFrameGrabberProxy.createGraProxy();
        this.asyncInit(robotId, width, height);

    }


    /**
     * 异步初始化{@link FFmpegFrameGrabberProxy} 代理类
     */
    private void asyncInit(int robotId, double width, double height) {

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
        final JsonObject json;
        var eventType = event.getEventType();
        //处理鼠标移动
        if (eventType == MouseEvent.MOUSE_MOVED) {
            var point = this.calculate(event);

            json = new JsonObject()
                    .put(Constants.X, point.getX())
                    .put(Constants.Y, point.getY())
                    .put(Constants.ACTION, MouseAction.MOUSE_MOVE);
        }
        //处理鼠标按下和释放操作
        else if (eventType == MouseEvent.MOUSE_PRESSED || eventType == MouseEvent.MOUSE_RELEASED) {
            var clickBtn = event.getButton();
            final MouseAction action;
            if (eventType == MouseEvent.MOUSE_PRESSED) {
                action = MouseAction.MOUSE_PRESSED;
            } else {
                action = MouseAction.MOUSE_RELEASED;
            }
            json = new JsonObject()
                    .put(Constants.ACTION, action)
                    .put(Constants.MOUSE_BTN, clickBtn);
        }
        //处理鼠标拖拽
        else if (eventType == MouseEvent.MOUSE_DRAGGED) {
            var point = calculate(event);
            json = new JsonObject()
                    .put(Constants.X, point.getX())
                    .put(Constants.Y, point.getY())
                    .put(Constants.ACTION,MouseAction.MOUSE_DRAGGED);
        }
        //不做处理
        else {
            json = null;
        }

        if (json == null) {
            return;
        }
        var buffer = SocketDataEncode.restRequest(
                SocketCMD.MOUSE_ACTIVE,
                XTApp.getInstance().getRemoteCode(),
                json
        );
        MainViewController.newInstance().getXtClient().send(buffer);
    }

    private Point2D calculate(MouseEvent event) {
        var viewport = scrollPane.getViewportBounds();
        var lh = this.width - viewport.getWidth();
        var lv = this.height - viewport.getHeight();
        if (lh < 0) {
            lh = 0;
        }
        if (lv < 0) {
            lv = 0;
        }
        //获取垂直滚动偏移量
        var scrollV = this.scrollPane.getVvalue() * lv;
        //获取水平滚动偏移量
        var scrollH = this.scrollPane.getHvalue() * lh;

        //如果目标显示器分比率小于默认窗体大小,计算VBox与canvas之间的间隔
        var hs = (this.contentBox.getWidth() - this.canvas.getWidth()) / 2;
        var vs = (this.contentBox.getHeight() - this.canvas.getHeight()) / 2;


        var x = event.getSceneX() + scrollH - hs;
        var y = event.getSceneY() + scrollV - this.topBox.getHeight() - vs;

        return new Point2D(x, y);
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

    @FXML
    private void requestWSize() {
        final double width;
        final double height;
        final Image image;
        if (maxWin) {
            image = MAX_ICON;
            var rect = UIUtil.getSrnSize();
            width = rect.getWidth() * wScala;
            height = rect.getHeight() * wScala;
        } else {
            image = MAX_ACTIVE_ICON;
            var rect = UIUtil.getVSrnSize();
            width = rect.getWidth();
            height = rect.getHeight();

        }
        this.maxWin = !this.maxWin;

        this.getStage().setWidth(width);
        this.getStage().setHeight(height);
        //重定位窗口位置
        if (!this.maxWin) {
            this.getStage().centerOnScreen();
        } else {
            this.getStage().setX(0);
            this.getStage().setY(0);
        }
        this.wsBtn.setGraphic(new ImageView(image));
    }

    @FXML
    private void requestClose() {
        this.triggerClose(false);
    }

    @FXML
    private void requestIconified() {
        this.getStage().setIconified(true);
    }

    @Override
    public void onRequestClose(WindowEvent event) {
        super.onRequestClose(event);
        this.fProxy.stop();
        MainViewController.newInstance().openWindow();
    }
}
