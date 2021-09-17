package cn.navclub.xtm.app;

import cn.navclub.xtm.app.controller.WinMonitorController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 *
 * XTerminal程序启动类
 *
 *
 * @author yangkui
 *
 */
public class XTM extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new WinMonitorController().openWindow();
    }
}
