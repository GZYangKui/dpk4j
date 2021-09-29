package cn.navclub.xtm.kit.client;

import cn.navclub.xtm.kit.enums.SocketCMD;
import io.vertx.core.buffer.Buffer;

public interface XTClientListener {
    /**
     * {@link  XTClient} 状态发生改变回调此方法
     */
    default void statusHandler(XTClientStatus oldStatus, XTClientStatus newStatus) {

    }

    /**
     * 消息收到时触发该函数
     *
     * @param cmd    消息类型
     * @param buffer 原始响应数据
     * @param data   原始响应数据中的数据
     */
    default void onMessage(SocketCMD cmd, Buffer buffer, Buffer data) {

    }

}
