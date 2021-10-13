package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.app.control.MainWinControl;
import cn.navclub.xtm.app.control.NavListItem;

import cn.navclub.xtm.app.controller.control.RemoteInfoController;
import cn.navclub.xtm.core.encode.SocketDataEncode;
import cn.navclub.xtm.core.enums.SocketCMD;
import cn.navclub.xtm.kit.client.XTClient;
import cn.navclub.xtm.kit.client.XTClientBuilder;
import cn.navclub.xtm.kit.client.XTClientListener;
import cn.navclub.xtm.kit.client.XTClientStatus;

import io.vertx.core.Vertx;
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

public class MainViewController extends AbstractWindowFXMLController<BorderPane> implements XTClientListener {
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

    private MainViewController() {
        super("MainView.fxml");

        this.listView.getItems().addAll(NavListItem.create());
        this.contentPane.setTop(new MainWinControl(this));

        this.getStage().setResizable(false);
        this.getStage().setTitle("朝天椒远程连接");
        this.getStage().initStyle(StageStyle.UNDECORATED);

        this.xtClient = XTClientBuilder
                .newBuilder(Vertx.vertx())
                .setHost(XTApp.getInstance().getHost())
                .setPort(XTApp.getInstance().getPort())
                .build();

        this.xtClient.addListener(this);

        this.xtClient.connect().onComplete(it -> {
            var buffer = SocketDataEncode.restRequest(SocketCMD.HEART_BEAT, 0,null);
            this.xtClient.send(buffer);
        });

        this.listView.getSelectionModel().selectedItemProperty().addListener(this.listItemChangeListener);
        this.listView.getSelectionModel().select(0);
    }

    private ChangeListener<NavListItem> listItemChangeListener() {
        return (observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.updateStatus(false);
            }
            newValue.updateStatus(true);
            //todo 切换tab
            this.contentPane.setCenter(new RemoteInfoController(this).getParent());
        };
    }

    @Override
    public void onRequestClose(WindowEvent event) {
        super.onRequestClose(event);
        if (this.xtClient != null) {
            xtClient.close();
            xtClient.getVertx().close();
        }
        this.listView.getSelectionModel().selectedItemProperty().removeListener(this.listItemChangeListener);
    }

    @Override
    public void statusHandler(XTClient client, XTClientStatus oldStatus, XTClientStatus newStatus) {
        var text = newStatus.getMessage();
        final String hexStr;
        if (newStatus == XTClientStatus.CONNECTED || newStatus == XTClientStatus.CONNECTING) {
            hexStr = "#53d115";
        } else if (newStatus == XTClientStatus.NOT_CONNECT) {
            hexStr = "#999999";
        } else {
            hexStr = "#FF0042";
        }
        Platform.runLater(() -> {
            this.sLabel.setText(text);
            this.sDot.setStroke(Color.web(hexStr));
        });
    }

    private static MainViewController controller;

    public synchronized static MainViewController newInstance() {
        if (controller == null) {
            controller = new MainViewController();
        }
        return controller;
    }

    public XTClient getXtClient() {
        return xtClient;
    }
}
