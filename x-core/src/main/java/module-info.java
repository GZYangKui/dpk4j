module cn.navclub.xtm.app.core {
    requires io.vertx.core;
    requires org.tukaani.xz;
    requires org.apache.commons.compress;
    exports cn.navclub.xtm.core.util;
    exports cn.navclub.xtm.core.enums;
    exports cn.navclub.xtm.core.encode;
    exports cn.navclub.xtm.core.decode;
    exports cn.navclub.xtm.core.function;
    exports cn.navclub.xtm.core.decode.impl;

}