module cn.navclub.xtm.kit {
    requires io.vertx.core;
    requires cn.navclub.xtm.app.core;

    requires transitive org.slf4j;
    requires transitive ch.qos.logback.classic;
    requires transitive org.bytedeco.javacv;
    requires transitive org.bytedeco.opencv;
    requires transitive org.bytedeco.ffmpeg;
    requires transitive org.bytedeco.javacpp;
    requires transitive org.bytedeco.openblas;

    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;

    //根据各个平台自由切换
    requires transitive org.bytedeco.ffmpeg.linux.x86_64;
    requires transitive org.bytedeco.opencv.linux.x86_64;
    requires transitive org.bytedeco.openblas.linux.x86_64;

    exports cn.navclub.xtm.kit;
    exports cn.navclub.xtm.kit.proxy;
    exports cn.navclub.xtm.kit.client;
    exports cn.navclub.xtm.kit.enums;
    exports cn.navclub.xtm.kit.listener;
    exports cn.navclub.xtm.kit.proxy.impl;
}