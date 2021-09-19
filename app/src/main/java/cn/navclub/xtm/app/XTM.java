package cn.navclub.xtm.app;

import cn.navclub.xtm.app.controller.WinMonitorController;
import javafx.application.Application;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOG = LoggerFactory.getLogger(XTM.class);

    @Override
    public void start(Stage primaryStage) {
        new WinMonitorController().openWindow();
    }

    public static void main(String[] args) {
//        System.setProperty("org.bytedeco.javacpp.logger.debug","true");
        launch(args);
    }
}
