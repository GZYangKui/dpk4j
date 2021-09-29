package cn.navclub.xtm.app.controller.control;

import cn.navclub.xtm.app.base.AbstractFXMLController;
import cn.navclub.xtm.app.config.Constants;
import cn.navclub.xtm.app.controller.MainViewController;
import cn.navclub.xtm.kit.client.XTClientListener;
import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.enums.SocketCMD;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;

/**
 * 远程连接配置界面
 */
public class RemoteInfoController extends AbstractFXMLController<VBox> implements XTClientListener {
    @FXML
    private Label robotCode;

    private volatile int code;

    private final MainViewController controller;

    public RemoteInfoController(MainViewController controller) {
        super("control/RemoteInfoView.fxml");
        this.controller = controller;
        this.setStyleSheet("control/RemoteInfoViewStyle.css");
        this.controller.getXtClient().addListener(this);
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
        var str = format.format(code).replaceAll(","," ");
        Platform.runLater(() -> {
            this.robotCode.setText(str);
        });
    }
}
