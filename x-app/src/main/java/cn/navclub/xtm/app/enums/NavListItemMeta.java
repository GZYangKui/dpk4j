package cn.navclub.xtm.app.enums;

import cn.navclub.xtm.app.AssetsHelper;
import javafx.scene.image.Image;

/**
 * 枚举{@link cn.navclub.xtm.app.controller.MainViewController}左侧导航栏信息
 */
public enum NavListItemMeta {
    REMOTE_CONNECT(
            "远程连接",
            AssetsHelper.loadImg("remote.png"),
            AssetsHelper.loadImg("remote_active.png")
    );
    /**
     * item 名称
     */
    private final String title;
    /**
     * 非激活状态图标
     */
    private final Image img;
    /**
     * 激活状态图标
     */
    private final Image activeImg;

    NavListItemMeta(String title, Image img, Image activeImg) {
        this.title = title;
        this.img = img;
        this.activeImg = activeImg;
    }

    public String getTitle() {
        return title;
    }

    public Image getImg() {
        return img;
    }

    public Image getActiveImg() {
        return activeImg;
    }
}
