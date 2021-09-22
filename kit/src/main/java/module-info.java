module cn.navclub.xtm.kit {
    requires org.slf4j;
    requires org.bytedeco.javacv;
    requires org.bytedeco.opencv;
    requires org.bytedeco.ffmpeg;
    requires org.bytedeco.javacpp;
    requires org.bytedeco.openblas;

    requires static io.vertx.core;

    //根据各个平台自由切换
    requires org.bytedeco.ffmpeg.linux.x86_64;
    requires org.bytedeco.opencv.linux.x86_64;
    requires org.bytedeco.openblas.linux.x86_64;


    exports cn.navclub.xtm.kit.util;
    exports cn.navclub.xtm.kit.proxy;
    exports cn.navclub.xtm.kit.decode;
    exports cn.navclub.xtm.kit.proxy.impl;
}