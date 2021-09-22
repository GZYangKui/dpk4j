package cn.navclub.xtm.app;

import cn.navclub.xtm.app.controller.WinMonitorController;
import javafx.application.Application;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * XTerminal程序启动类
 *
 * @author yangkui
 */
public class XTM extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(XTM.class);

    public static void main(String[] args) {
//        System.setProperty("org.bytedeco.javacpp.logger.debug","true");
        var optional = ModuleLayer.boot().findModule("org.bytedeco.ffmpeg.linux.x86_64");
        optional.ifPresentOrElse(it->System.out.println(it.getName()+":存在"),()->{
            System.out.println("模块不存在");
        });
        for (Module module : ModuleLayer.boot().modules()) {
            System.out.println("module name:" + module.getName());
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new WinMonitorController().openWindow();
    }

}
