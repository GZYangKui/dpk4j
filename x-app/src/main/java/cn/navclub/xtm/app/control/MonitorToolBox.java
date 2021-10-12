package cn.navclub.xtm.app.control;

import cn.navclub.xtm.app.AssetsHelper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MonitorToolBox extends BorderPane {
    private static final String DEFAULT_STYLE_CLASS = "tool-box";
    private static final Image UP = AssetsHelper.loadImg("tool-box/up.png");
    private static final Image DOWN = AssetsHelper.loadImg("tool-box/down.png");

    private boolean up;
    private double prefHeight = 70.0;

    private final VBox cBox;
    private final HBox leftBox;
    private final HBox rightBox;
    private final Button switcher;

    private final List<Button> buttons;

    public MonitorToolBox() {

        this.cBox = new VBox();
        this.switcher = new Button();
        this.rightBox = new HBox();
        this.leftBox = new HBox();
        this.buttons = new ArrayList<>();

        this.switcher.setGraphic(new ImageView(UP));

        this.cBox.setAlignment(Pos.BOTTOM_CENTER);
        for (ToolBoxItem value : ToolBoxItem.values()) {
            var btn = new Button(value.title);
            btn.setContentDisplay(ContentDisplay.TOP);
            var image = AssetsHelper.loadImg("tool-box/" + value.icon);
            btn.setGraphic(new ImageView(image));
            this.buttons.add(btn);
            this.leftBox.getChildren().add(btn);
        }
        this.cBox.getChildren().add(this.switcher);
        this.setMaxHeight(this.prefHeight);
        this.setCenter(cBox);
        this.setLeft(leftBox);
        this.setRight(rightBox);
        this.leftBox.getStyleClass().add("left-box");
        this.cBox.getStyleClass().add("center-box");
        this.switcher.setOnAction(event -> this.updateStatus());
        this.rightBox.prefWidthProperty().bind(this.leftBox.widthProperty());
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        StackPane.setAlignment(this, Pos.TOP_CENTER);
    }

    private void updateStatus() {
        this.up = !this.up;
        final double mw;
        final double mh;
        final Image image;
        if (up) {
            image = DOWN;
            this.leftBox.getChildren().clear();
            mh = this.switcher.getHeight();
            mw = this.switcher.getWidth();
        } else {
            mw = -1;
            image = UP;
            mh = this.prefHeight;
            this.leftBox.getChildren().addAll(this.buttons);
        }
        this.setMaxWidth(mw);
        this.setMaxHeight(mh);
        this.switcher.setGraphic(new ImageView(image));
    }

    private enum ToolBoxItem {
        CHAT("chat.png", "聊天"),
        RECORD("record.png", "录像"),
        SNAP("snap.png", "截幕");

        private final String icon;
        private final String title;

        ToolBoxItem(String icon, String title) {
            this.icon = icon;
            this.title = title;
        }
    }
}
