package cn.navclub.xtm.app;

import cn.navclub.xtm.app.controller.MainViewController;
import javafx.application.Application;

import javafx.stage.Stage;


/**
 * XTerminal程序启动类
 *
 * @author yangkui
 */
public class XTM extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new MainViewController().openWindow();
    }

}
