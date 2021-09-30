package cn.navclub.xtm.app.controller.control;

import cn.navclub.xtm.app.base.AbstractFXMLController;
import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.controller.MainViewController;
import cn.navclub.xtm.kit.client.XTClient;
import cn.navclub.xtm.kit.client.XTClientListener;
import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.encode.SocketDataEncode;
import cn.navclub.xtm.kit.enums.ClientStatus;
import cn.navclub.xtm.kit.enums.SocketCMD;
import cn.navclub.xtm.kit.enums.TCPDirection;
import cn.navclub.xtm.kit.util.StrUtil;
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
        this.controller.getXtClient().addListener(this);
        var update = new MenuItem("更新验证码");
        var custom = new MenuItem("自定义验证码");

        editBtn.getItems().addAll(update, custom);
        //随机生成秘钥
        update.setOnAction(event -> this.rdPw());
    }

    private void rdPw(){
        var str = StrUtil.rdStr(6, true);
        XTApp.getInstance().setRobotPw(str);
        this.password.setText(str);
    }

    @Override
    public void onMessage(XTClient client, RecordParser.Record record) {
        //执行更新操作
        if (record.cmd() == SocketCMD.UPDATE_CLIENT_CODE) {
            var json = record.toJson();
            var code = json.getString(Constants.CODE);
            //更新验证码
            this.updateCode(Integer.parseInt(code));
        }
        if (record.cmd() == SocketCMD.REQUEST_REMOTE && record.direction() == TCPDirection.RESPONSE) {
            if (record.status()== ClientStatus.OFFLINE){
                Platform.runLater(()->{
                    Notifications.create().text("目标主机离线!").position(Pos.TOP_RIGHT).showWarning();
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
        if (code==XTApp.getInstance().getRobotCode()){
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
}
