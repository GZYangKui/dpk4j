package cn.navclub.xtm.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * 资源管理器
 *
 * @author yangkui
 */
public class AssetsHelper {
    public static final String CSS_ROOT = "css/";
    public static final String FXML_ROOT = "fxml/";
    public static final String IMG_ROOT = "img/";

    public static final Logger LOG = LoggerFactory.getLogger(AssetsHelper.class);

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


    public static <T extends Parent> void addStyleSheets(T parent,String...sheets){
        for (String sheet : sheets) {
            if (!sheet.startsWith(CSS_ROOT)){
                sheet = CSS_ROOT+sheet;
            }
            var url = AssetsHelper.class.getResource(sheet);
            if (url == null){
                LOG.debug("StyleSheet {} not found.",sheet);
            }else {
                parent.getStylesheets().add(url.toExternalForm());
            }
        }
    }

    public static Image loadImg(String path){
        if (!path.startsWith(IMG_ROOT)){
            path = IMG_ROOT+path;
        }
        var url = AssetsHelper.class.getResource(path);
        if (url == null){
            LOG.info("Load local img [{}] failed",path);
            throw new RuntimeException("资源加载失败,请检查资源文件是否可用！");
        }
        try {
            return new Image(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
