package cn.navclub.xtm.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * 资源管理器
 *
 * @author yangkui
 */
public class AssetsHelper {
    public static final String FXML_ROOT = "fxml/";

    /**
     *
     * 加载FXML视图文件
     *
     */
    public static <T extends Parent> T loadFXMLView(URL url, Object controller) {
        assert url != null;
        var fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(controller);
        try {
           return fxmlLoader.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
