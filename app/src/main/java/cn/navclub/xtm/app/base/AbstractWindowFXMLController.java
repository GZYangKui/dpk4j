package cn.navclub.xtm.app.base;

import cn.navclub.xtm.app.AssetsHelper;
import cn.navclub.xtm.app.service.WindowControllerService;
import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public class AbstractWindowFXMLController<T extends Parent> extends AbstractFXMLController<T> implements WindowControllerService {
    private final Stage stage;

    private final ChangeListener<Number> widthListener = this.windowChangeListener(false);
    private final ChangeListener<Number> heightListener = this.windowChangeListener(true);

    public AbstractWindowFXMLController(String fxmlURL) {
        this(AssetsHelper.class.getResource(AssetsHelper.FXML_ROOT + fxmlURL));
    }

    public AbstractWindowFXMLController(URL fxmlURL) {
        super(fxmlURL);
        this.stage = new Stage();
        this.stage.setScene(new Scene(this.getParent()));
        //注册请求关闭事件
        this.stage.setOnCloseRequest(this::onRequestClose);
        this.stage.widthProperty().addListener(this.widthListener);
        this.stage.heightProperty().addListener(this.heightListener);
    }

    private ChangeListener<Number> windowChangeListener(boolean height){
        return (observable, oldValue, newValue) -> {
            if (height){
                this.windowSizeChange(this.stage.getWidth(),newValue.doubleValue());
            }else {
                this.windowSizeChange(this.stage.getHeight(),newValue.doubleValue());
            }
        };
    }

    @Override
    public void onRequestClose(WindowEvent event) {
        this.stage.widthProperty().removeListener(this.widthListener);
        this.stage.heightProperty().removeListener(this.heightListener);
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }
}
