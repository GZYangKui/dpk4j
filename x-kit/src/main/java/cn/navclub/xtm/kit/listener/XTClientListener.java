package cn.navclub.xtm.kit.listener;


import cn.navclub.xtm.core.decode.RecordParser;
import cn.navclub.xtm.core.enums.SocketCMD;
import cn.navclub.xtm.kit.client.XClient;
import cn.navclub.xtm.kit.client.impl.TCPClient;
import cn.navclub.xtm.kit.client.impl.UDPClient;
import cn.navclub.xtm.kit.enums.XTClientStatus;

import java.util.List;

public interface XTClientListener {
    /**
     * 默认不监听任何{@link SocketCMD} 事件
     */
    List<SocketCMD> EMPTY_ACTIONS = List.of();

    /**
     * {@link  TCPClient} 状态发生改变回调此方法
     */
    default void statusHandler(boolean udp,XClient client, XTClientStatus oldStatus, XTClientStatus newStatus) {

    }

    /**
     * 收到TCP消息时触发该函数
     */
    default void onMessage( boolean udp,XClient client, RecordParser.Record record) {

    }

    /**
     * 判断当前监听器需要监听哪些{@link SocketCMD}命令
     */
    default List<SocketCMD> actions() {
        return EMPTY_ACTIONS;
    }

    /**
     * 是否监听{@link TCPClient}状态变化
     */
    default boolean lStatus() {
        return false;
    }

    /**
     * 获取当前listener名称,默认返回当前类名称
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }

}
