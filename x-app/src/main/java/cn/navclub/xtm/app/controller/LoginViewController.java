package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import cn.navclub.xtm.app.control.WinControl;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

/**
 * 用户登录页面
 */
public class LoginViewController extends AbstractWindowFXMLController<BorderPane> {
    public LoginViewController() {
        super("LoginView.fxml");
        this.getParent().setTop(new WinControl<>(this));
        this.getScene().setFill(Color.TRANSPARENT);
        this.getStage().initStyle(StageStyle.TRANSPARENT);
    }

    @FXML
    private void login(){
        MainViewController.newInstance().openWindow();
        this.getStage().close();
    }
}
