module cn.navclub.xtm.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.slf4j;

    requires org.kordamp.ikonli.javafx;

    requires org.bytedeco.javacv;
    requires org.bytedeco.opencv;
    requires org.bytedeco.ffmpeg;
    requires org.bytedeco.javacpp;
    requires org.bytedeco.openblas;


    //根据各个平台自由切换
    requires org.bytedeco.ffmpeg.linux.x86_64;
    requires org.bytedeco.opencv.linux.x86_64;
    requires org.bytedeco.openblas.linux.x86_64;

    opens cn.navclub.xtm.app.base to javafx.fxml;
    opens cn.navclub.xtm.app.controller to javafx.fxml;

    exports cn.navclub.xtm.app;
}