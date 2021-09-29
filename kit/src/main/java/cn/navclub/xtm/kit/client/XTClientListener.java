package cn.navclub.xtm.kit.client;

import cn.navclub.xtm.kit.decode.RecordParser;

public interface XTClientListener {
    /**
     * {@link  XTClient} 状态发生改变回调此方法
     */
    default void statusHandler(XTClientStatus oldStatus, XTClientStatus newStatus) {

    }

    /**
     * 消息收到时触发该函数
     */
    default void onMessage(RecordParser.Record record) {

    }

}
