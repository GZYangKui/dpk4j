package cn.navclub.xtm.app.controller.control;

import cn.navclub.xtm.app.base.AbstractFXMLController;
import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.controller.MainViewController;
import cn.navclub.xtm.app.controller.WinGrabController;
import cn.navclub.xtm.app.controller.WinMonitorController;
import cn.navclub.xtm.app.util.UIUtil;
import cn.navclub.xtm.core.decode.RecordParser;
import cn.navclub.xtm.core.encode.SocketDataEncode;
import cn.navclub.xtm.core.enums.ClientStatus;
import cn.navclub.xtm.core.enums.SocketCMD;
import cn.navclub.xtm.core.enums.TCPDirection;
import cn.navclub.xtm.core.util.StrUtil;
import cn.navclub.xtm.kit.XLHelper;
import cn.navclub.xtm.kit.client.XClient;
import cn.navclub.xtm.kit.client.impl.TCPClient;

import cn.navclub.xtm.kit.listener.XTClientListener;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 远程连接配置界面
 */
public class RemoteInfoController extends AbstractFXMLController<VBox> implements XTClientListener {
    @FXML
    private Label robotCode;
    @FXML
    private Label password;
    @FXML
    private MenuButton editBtn;
    @FXML
    private TextField friendCode;
    @FXML
    private TextField friendPw;

    private final MainViewController controller;

    public RemoteInfoController(MainViewController controller) {
        super("control/RemoteInfoView.fxml");
        this.rdPw();
        this.controller = controller;
        XLHelper.addListener(this);
        var update = new MenuItem("更新验证码");
        var custom = new MenuItem("自定义验证码");

        editBtn.getItems().addAll(update, custom);
        //随机生成秘钥
        update.setOnAction(event -> this.rdPw());
    }

    private void rdPw() {
        var str = StrUtil.rdStr(6, true);
        XTApp.getInstance().setRobotPw(str);
        this.password.setText(str);
    }

    @Override
    public void onMessage(boolean udp, XClient client, RecordParser.Record record) {
        //执行更新操作
        if (record.getCmd() == SocketCMD.UPDATE_CLIENT_CODE) {
            var json = record.toJson();
            var code = json.getString(Constants.CODE);
            //更新验证码
            this.updateCode(Integer.parseInt(code));
        }
        //处理远程连接请求
        if (record.getCmd() == SocketCMD.REQUEST_REMOTE && record.getDirection() == TCPDirection.REQUEST) {
            var json = record.toJson();
            var pw = XTApp.getInstance().getRobotPw();
            var temp = json.getString(Constants.PASSWORD);
            final Buffer buffer;
            //口令错误
            if (!pw.equals(temp)) {
                buffer = SocketDataEncode.restResponse(
                        record.getCmd(),
                        ClientStatus.UNAUTHORIZED,
                        record.getSourceAddr(),
                        null
                );
            } else if (XTApp.getInstance().isRemoting()) {
                buffer = SocketDataEncode.restResponse(
                        record.getCmd(),
                        ClientStatus.CLIENT_BUSY,
                        record.getSourceAddr(),
                        null
                );
            } else {
                var rect = UIUtil.getSrnSize();
                buffer = SocketDataEncode.restResponse(
                        record.getCmd(),
                        ClientStatus.OK,
                        record.getSourceAddr(),
                        new JsonObject()
                                .put(Constants.WIDTH, rect.getWidth())
                                .put(Constants.HEIGHT, rect.getHeight())
                );
                XTApp.getInstance().setRemoteCode(record.getSourceAddr());
                Platform.runLater(() -> {
                    MainViewController.newInstance().getStage().hide();
                    new WinGrabController().openWindow();
                });
            }
            client.send(buffer);
        }
        //处理远程连接响应结果
        if (record.getCmd() == SocketCMD.REQUEST_REMOTE && record.getDirection() == TCPDirection.RESPONSE) {
            if (record.getStatus() != ClientStatus.OK) {
                Platform.runLater(() -> Notifications
                        .create()
                        .text(record.getStatus().getMessage())
                        .position(Pos.TOP_RIGHT).showWarning());
            } else {
                XTApp.getInstance().setRemoteCode(record.getSourceAddr());
                Platform.runLater(() -> {
                    MainViewController.newInstance().getStage().hide();
                    var data = record.toJson();
                    //获取目标显示器宽度
                    var width = data.getDouble(Constants.WIDTH);
                    //获取目标显示器高度
                    var height = data.getDouble(Constants.HEIGHT);
                    new WinMonitorController(record.getSourceAddr(), width, height).openWindow();
                });
            }
        }
    }


    private void updateCode(int code) {
        XTApp.getInstance().setRobotCode(code);
        var format = new DecimalFormat("###,###,###");
        var str = format.format(code).replaceAll(",", " ");
        Platform.runLater(() -> this.robotCode.setText(str));
    }

    @FXML
    private void requestRemote() {
        var str = this.friendCode.getText();
        var pw = this.friendPw.getText();
        if (StrUtil.isEmpty(str) || StrUtil.isEmpty(pw)) {
            Notifications
                    .create()
                    .text("识别码/验证码不能为空!")
                    .position(Pos.TOP_RIGHT).showWarning();
            return;
        }
        var code = Integer.parseInt(str);
        if (code == XTApp.getInstance().getRobotCode()) {
            Notifications
                    .create()
                    .text("不允许目标主机与当前主机一致!")
                    .position(Pos.TOP_RIGHT).showWarning();
            return;
        }
        var buf = SocketDataEncode.restRequest(
                SocketCMD.REQUEST_REMOTE,
                code,
                new JsonObject().put(Constants.PASSWORD, pw)
        );
        MainViewController.newInstance().getXtClient().send(buf);
    }

    private static final List<SocketCMD> ACTIONS = List.of(
            SocketCMD.UPDATE_CLIENT_CODE,
            SocketCMD.REQUEST_REMOTE
    );

    @Override
    public List<SocketCMD> actions() {
        return ACTIONS;
    }
}
