package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.Notifications;

public class WinConfigController extends AbstractWindowFXMLController<BorderPane> {
    @FXML
    private TextField screenTF;
    @FXML
    private TextField serverTF;

    public WinConfigController() {
        super("WinConfigView.fxml");
        this.setStyleSheet("WinConfigViewStyle.css");
        this.getStage().setResizable(false);
        this.getStage().setTitle("基础配置");
    }

    @FXML
    public void submit(){
        var screenId = this.screenTF.getText().trim();
        var serverIP = this.serverTF.getText().trim();
        if (screenId.equals("") || serverIP.equals("")){
            Notifications.create().position(Pos.TOP_RIGHT).text("请完善配置信息!").showInformation();
            return;
        }
        new WinMonitorController(serverIP,screenId).openWindow();
        this.getStage().close();
    }
}
