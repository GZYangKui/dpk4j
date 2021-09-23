package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import cn.navclub.xtm.app.control.MainWinControl;
import cn.navclub.xtm.app.control.NavListItem;

import cn.navclub.xtm.app.controller.control.RemoteInfoController;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;

import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainViewController extends AbstractWindowFXMLController<BorderPane> {
    @FXML
    private BorderPane contentPane;
    @FXML
    private ListView<NavListItem> listView;

    private final ChangeListener<NavListItem> listItemChangeListener = this.listItemChangeListener();

    public MainViewController() {
        super("MainView.fxml");

        this.listView.getItems().addAll(NavListItem.create());
        this.contentPane.setTop(new MainWinControl(this));

        this.setStyleSheet("MainViewStyle.css");
        this.getStage().setResizable(false);
        this.getStage().setTitle("朝天椒远程连接");
        this.getStage().initStyle(StageStyle.UNDECORATED);

        this.listView.getSelectionModel().selectedItemProperty().addListener(this.listItemChangeListener);
        this.listView.getSelectionModel().select(0);
    }

    private ChangeListener<NavListItem> listItemChangeListener(){
        return (observable, oldValue, newValue) -> {
            if (oldValue!=null) {
                oldValue.updateStatus(false);
            }
            newValue.updateStatus(true);
            //todo 切换tab
            this.contentPane.setCenter(new RemoteInfoController().getParent());
        };
    }

    @Override
    public void onRequestClose(WindowEvent event) {
        super.onRequestClose(event);
        this.listView.getSelectionModel().selectedItemProperty().removeListener(this.listItemChangeListener);
    }

}
