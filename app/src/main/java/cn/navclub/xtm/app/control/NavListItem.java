package cn.navclub.xtm.app.control;

import cn.navclub.xtm.app.enums.NavListItemMeta;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NavListItem extends HBox {
    private static final String DEFAULT_STYLE_CLASS = "nav-list-item";

    private final Label label;
    private final NavListItemMeta listItem;

    public NavListItem(NavListItemMeta listItem) {
        this.listItem = listItem;
        this.label = new Label(listItem.getTitle());
        this.getChildren().add(label);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);

        this.updateStatus(false);
    }

    public NavListItemMeta getListItem() {
        return listItem;
    }

    public void updateStatus(boolean active){
        var img = listItem.getImg();
        if (active){
            img = listItem.getActiveImg();
        }
        this.label.setGraphic(new ImageView(img));
    }

    public static List<NavListItem> create(){
        return Arrays.stream(NavListItemMeta.values()).map(NavListItem::new).collect(Collectors.toList());
    }
}
