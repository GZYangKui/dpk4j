package cn.navclub.xtm.app.control;

import cn.navclub.xtm.app.AssetsHelper;
import cn.navclub.xtm.app.controller.MainViewController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 *
 *
 * {@link cn.navclub.xtm.app.controller.MainViewController} 窗口控制器(功能键、最小化、关闭)
 *
 *
 */
public class MainWinControl extends HBox {
    private static final String DEFAULT_STYLE_CLASS = "win_control";

    private final MainViewController controller;

    public MainWinControl(final MainViewController controller) {
        this.controller = controller;

        var func = new MenuButton();
        var min = new Button();
        var close = new Button();


        min.setGraphic(new ImageView(AssetsHelper.loadImg("win/min@1x.png")));
        close.setGraphic(new ImageView(AssetsHelper.loadImg("win/close@1x.png")));

        this.setAlignment(Pos.CENTER_RIGHT);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        close.getStyleClass().add("win-close");
        this.getChildren().addAll(func,min,close);

        close.setOnAction(event -> this.controller.getStage().close());
        min.setOnAction(event -> this.controller.getStage().setIconified(true));
    }


}
