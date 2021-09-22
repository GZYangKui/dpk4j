module cn.navclub.xtm.kit {
    requires transitive org.slf4j;
    requires transitive org.bytedeco.javacv;
    requires transitive org.bytedeco.opencv;
    requires transitive org.bytedeco.ffmpeg;
    requires transitive org.bytedeco.javacpp;
    requires transitive org.bytedeco.openblas;

    requires static io.vertx.core;

    //根据各个平台自由切换
    requires transitive org.bytedeco.ffmpeg.linux.x86_64;
    requires transitive org.bytedeco.opencv.linux.x86_64;
    requires transitive org.bytedeco.openblas.linux.x86_64;


    exports cn.navclub.xtm.kit.util;
    exports cn.navclub.xtm.kit.proxy;
    exports cn.navclub.xtm.kit.decode;
    exports cn.navclub.xtm.kit.proxy.impl;
}