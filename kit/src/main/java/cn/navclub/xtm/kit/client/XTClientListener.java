package cn.navclub.xtm.kit.client;

import cn.navclub.xtm.kit.decode.RecordParser;
import cn.navclub.xtm.kit.enums.SocketCMD;

import java.util.List;

public interface XTClientListener {
    /**
     * 默认不监听任何{@link SocketCMD} 事件
     */
    List<SocketCMD> EMPTY_ACTIONS = List.of();

    /**
     * {@link  XTClient} 状态发生改变回调此方法
     */
    default void statusHandler(XTClient client, XTClientStatus oldStatus, XTClientStatus newStatus) {

    }

    /**
     * 消息收到时触发该函数
     */
    default void onMessage(XTClient client, RecordParser.Record record) {

    }

    /**
     * 判断当前监听器需要监听哪些{@link SocketCMD}命令
     */
    default List<SocketCMD> actions() {
        return EMPTY_ACTIONS;
    }

}
