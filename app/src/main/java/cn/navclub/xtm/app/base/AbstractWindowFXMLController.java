package cn.navclub.xtm.app.base;

import cn.navclub.xtm.app.AssetsHelper;
import cn.navclub.xtm.app.service.WindowControllerService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class AbstractWindowFXMLController<T extends Parent> extends AbstractFXMLController<T> implements WindowControllerService {
    private final Stage stage;


    public AbstractWindowFXMLController(String fxmlURL) {
        this(AssetsHelper.class.getResource(AssetsHelper.FXML_ROOT + fxmlURL));
    }

    public AbstractWindowFXMLController(URL fxmlURL) {
        super(fxmlURL);
        this.stage = new Stage();
        this.stage.setScene(new Scene(this.getParent()));
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }
}
