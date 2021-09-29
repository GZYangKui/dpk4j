package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import cn.navclub.xtm.app.control.MainWinControl;
import cn.navclub.xtm.app.control.NavListItem;

import cn.navclub.xtm.app.controller.control.RemoteInfoController;
import cn.navclub.xtm.kit.client.XTClient;
import cn.navclub.xtm.kit.client.XTClientBuilder;
import cn.navclub.xtm.kit.client.XTClientListener;
import cn.navclub.xtm.kit.client.XTClientStatus;
import cn.navclub.xtm.kit.enums.SocketCMD;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainViewController extends AbstractWindowFXMLController<BorderPane> {
    @FXML
    private Circle sDot;
    @FXML
    private Label sLabel;
    @FXML
    private BorderPane contentPane;
    @FXML
    private ListView<NavListItem> listView;

    private final XTClient xtClient;

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

        this.xtClient = XTClientBuilder
                .newBuilder(Vertx.vertx())
                .setHost("127.0.0.1")
                .setPort(8888)
                .build();

        this.xtClient.connect().onComplete(it->{
            this.xtClient.send(SocketCMD.HEART_BEAT,new JsonObject());
        });

        this.xtClient.addListener(this.listener());
    }

    private ChangeListener<NavListItem> listItemChangeListener() {
        return (observable, oldValue, newValue) -> {
            if (oldValue != null) {
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
        if (this.xtClient != null) {
            xtClient.close();
        }
        this.listView.getSelectionModel().selectedItemProperty().removeListener(this.listItemChangeListener);
    }

    public XTClientListener listener() {
        var that = this;
        return new XTClientListener() {

            @Override
            public void onMessage(SocketCMD cmd, Buffer buffer, Buffer data) {

            }

            @Override
            public void statusHandler(XTClientStatus oldStatus, XTClientStatus newStatus) {
                var text = newStatus.getMessage();
                final String hexStr;
                if (newStatus == XTClientStatus.CONNECTED || newStatus == XTClientStatus.CONNECTING) {
                    hexStr = "#53d115";
                }else if (newStatus == XTClientStatus.NOT_CONNECT){
                    hexStr = "#999999";
                }else {
                    hexStr = "#FF0042";
                }
                Platform.runLater(()->{
                    that.sLabel.setText(text);
                    that.sDot.setStroke(Color.web(hexStr));
                });
            }
        };
    }

}
