package cn.navclub.xtm.app.base;

import cn.navclub.xtm.app.AssetsHelper;
import javafx.scene.Parent;

import java.net.URL;

public class AbstractFXMLController<T extends Parent> {

    private final T parent;

    public AbstractFXMLController(URL fxmlUrl) {
        this.parent = AssetsHelper.loadFXMLView(fxmlUrl, this);
    }

    public T getParent() {
        return parent;
    }
}
