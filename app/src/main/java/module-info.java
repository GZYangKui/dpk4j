module cn.navclub.xtm.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires cn.navclub.xtm.kit;

    requires io.vertx.core;
    requires org.kordamp.ikonli.javafx;

    requires org.bytedeco.javacv;
    requires org.bytedeco.opencv;
    requires org.bytedeco.ffmpeg;
    requires org.bytedeco.javacpp;
    requires org.bytedeco.openblas;


    opens cn.navclub.xtm.app.base to javafx.fxml;
    opens cn.navclub.xtm.app.control to javafx.fxml;
    opens cn.navclub.xtm.app.controller to javafx.fxml;
    opens cn.navclub.xtm.app.controller.control to javafx.fxml;

    exports cn.navclub.xtm.app;
}