package cn.navclub.xtm.app;

import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.controller.MainViewController;
import cn.navclub.xtm.app.controller.WinMonitorController;
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
        XTApp.getInstance()
                .setHost("192.168.0.104")
                .setPort(8888);
        MainViewController.newInstance().openWindow();
//        new WinMonitorController(111,1850,960).openWindow();
    }

}
