package cn.navclub.xtm.app.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class UIUtil {
    private static final Screen SCREEN = Screen.getPrimary();

    /**
     *
     * 获取屏幕尺寸
     *
     */
    public static Rectangle2D getSrnSize(){
        return SCREEN.getBounds();
    }

    /**
     *
     *  获取屏幕可见区域尺寸
     *
     */
    public static Rectangle2D getVSrnSize(){
        return SCREEN.getVisualBounds();
    }
}
