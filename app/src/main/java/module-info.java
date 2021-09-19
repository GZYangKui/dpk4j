module cn.navclub.xtm.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.slf4j;

    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    requires org.bytedeco.javacv;
    requires org.bytedeco.opencv;
    requires org.bytedeco.ffmpeg;
    requires org.bytedeco.javacpp;
    requires org.bytedeco.openblas;


    //根据各个平台自由切换
    requires org.bytedeco.ffmpeg.linux.x86_64;

    opens cn.navclub.xtm.app to javafx.fxml;

    exports cn.navclub.xtm.app;
    exports cn.navclub.xtm.app.controller;
    opens cn.navclub.xtm.app.controller to javafx.fxml;
    exports cn.navclub.xtm.app.base;
    opens cn.navclub.xtm.app.base to javafx.fxml;
}