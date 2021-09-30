package cn.navclub.xtm.app;

import cn.navclub.xtm.app.config.XTApp;
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
        for (Module module : ModuleLayer.boot().modules()) {
            System.out.println(module.getName());
        }
        System.setProperty("org.bytedeco.javacpp.logger.debug","true");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        XTApp.getInstance()
                .setHost("192.168.0.104")
                .setPort(8888);
        MainViewController.newInstance().openWindow();
    }

}
