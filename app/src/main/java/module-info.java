module cn.navclub.xtm.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens cn.navclub.xtm.app to javafx.fxml;

    exports cn.navclub.xtm.app;
    exports cn.navclub.xtm.app.controller;
    opens cn.navclub.xtm.app.controller to javafx.fxml;
    exports cn.navclub.xtm.app.base;
    opens cn.navclub.xtm.app.base to javafx.fxml;
}