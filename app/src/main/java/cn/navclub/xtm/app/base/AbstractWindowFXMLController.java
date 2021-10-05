package cn.navclub.xtm.app.base;

import cn.navclub.xtm.app.AssetsHelper;
import cn.navclub.xtm.app.service.WindowControllerService;
import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public class AbstractWindowFXMLController<T extends Parent> extends AbstractFXMLController<T> implements WindowControllerService {
    private static final Image[] LOGOS = new Image[]{
            AssetsHelper.loadImg("logo@1x.png"),
            AssetsHelper.loadImg("logo@2x.png"),
            AssetsHelper.loadImg("logo@3x.png")
    };

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

        this.stage.getIcons().addAll(LOGOS);
    }

    private ChangeListener<Number> windowChangeListener(boolean height) {
        return (observable, oldValue, newValue) -> {
            if (height) {
                this.windowSizeChange(this.stage.getWidth(), newValue.doubleValue());
            } else {
                this.windowSizeChange(this.stage.getHeight(), newValue.doubleValue());
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

    /**
     * 用于手动触发窗口关闭并触发{@link AbstractWindowFXMLController#onRequestClose(WindowEvent)}方法处理资源释放问题.</p>
     */
    public void triggerClose(boolean emitEx) {
        var event = new WindowEvent(
                this.getStage(),
                WindowEvent.WINDOW_HIDING
        );
        try {
            this.onRequestClose(event);
        } catch (Exception e) {
            if (emitEx) {
                throw new RuntimeException(e);
            }
        }
        //执行关闭窗口
        if (!event.isConsumed()) {
            this.getStage().close();
        }
    }
}
