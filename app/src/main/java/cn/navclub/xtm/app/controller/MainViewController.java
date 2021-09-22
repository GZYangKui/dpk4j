package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import cn.navclub.xtm.app.control.MainWinControl;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;
import org.controlsfx.control.Notifications;

public class MainViewController extends AbstractWindowFXMLController<BorderPane> {
    @FXML
    private TextField screenTF;
    @FXML
    private TextField serverTF;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private BorderPane contentPane;

    public MainViewController() {
        super("MainView.fxml");

        this.contentPane.setTop(new MainWinControl(this));

        this.setStyleSheet("MainViewStyle.css");
        this.getStage().setResizable(false);
        this.getStage().setTitle("朝天椒远程连接");
        this.getStage().initStyle(StageStyle.UNDECORATED);
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
