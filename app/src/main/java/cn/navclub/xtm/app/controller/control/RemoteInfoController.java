package cn.navclub.xtm.app.controller.control;

import cn.navclub.xtm.app.base.AbstractFXMLController;
import javafx.scene.layout.VBox;

/**
 * 远程连接配置界面
 */
public class RemoteInfoController extends AbstractFXMLController<VBox> {
    public RemoteInfoController() {
        super("control/RemoteInfoView.fxml");
        this.setStyleSheet("control/RemoteInfoViewStyle.css");
    }
}
