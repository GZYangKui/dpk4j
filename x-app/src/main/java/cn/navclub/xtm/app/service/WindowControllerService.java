package cn.navclub.xtm.app.service;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public interface WindowControllerService {
    /**
     * 获取当前窗口实例对象
     */
    Stage getStage();

    /**
     * 显示窗口
     */
    default void openWindow() {
        var stage = this.getStage();
        stage.show();
        stage.toFront();
    }

    /**
     * 获取{@link Scene}对象
     */
    default Scene getScene() {
        return this.getStage().getScene();
    }

    /**
     * 当窗口请求关闭时调用该函数
     */
    default void onRequestClose(WindowEvent event) {

    }

    /**
     *
     * 当窗口尺寸发生改变时回到该函数
     *
     */
    default void windowSizeChange(double width,double height){

    }

    /**
     *
     * 当窗口显示状态发生改变时触发该函数回调
     *
     */
    default void stageShowChange(boolean oldValue,boolean newValue){

    }
}
