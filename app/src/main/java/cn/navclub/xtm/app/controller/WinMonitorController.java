package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import javafx.scene.layout.BorderPane;

/**
 *
 * 远程操作主窗口
 *
 *
 * @author yangkui
 *
 */
public class WinMonitorController extends AbstractWindowFXMLController<BorderPane> {
    public WinMonitorController() {
        super("WinMonitorView.fxml");
        this.getStage().setTitle("x-terminal");
    }
}
