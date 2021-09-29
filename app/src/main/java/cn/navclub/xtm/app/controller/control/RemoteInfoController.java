package cn.navclub.xtm.app.controller.control;

import cn.navclub.xtm.app.base.AbstractFXMLController;
import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.controller.MainViewController;
import cn.navclub.xtm.kit.client.XTClientListener;
import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.enums.SocketCMD;
import cn.navclub.xtm.kit.util.StrUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

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

    private volatile int code;

    private final MainViewController controller;

    public RemoteInfoController(MainViewController controller) {
        super("control/RemoteInfoView.fxml");
        this.controller = controller;
        this.controller.getXtClient().addListener(this);
        var update = new MenuItem("更新验证码");
        var custom = new MenuItem("自定义验证码");

        editBtn.getItems().addAll(update, custom);

        //随机生成秘钥
        update.setOnAction(event -> {
            var str = StrUtil.rdStr(6,true);
            this.password.setText(str);
        });
    }

    @Override
    public void onMessage(RecordParser.Record record) {
        //执行更新操作
        if (record.cmd() == SocketCMD.UPDATE_CLIENT_CODE) {
            var json = record.toJson();
            var code = json.getString(Constants.CODE);
            //更新验证码
            this.updateCode(Integer.parseInt(code));
        }
    }


    private void updateCode(int code) {
        this.code = code;
        var format = new DecimalFormat("###,###,###");
        var str = format.format(code).replaceAll(",", " ");
        Platform.runLater(() -> {
            this.robotCode.setText(str);
        });
    }
}
