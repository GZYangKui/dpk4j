package cn.navclub.xtm.kit.listener;


import cn.navclub.xtm.core.decode.RecordParser;
import cn.navclub.xtm.core.enums.SocketCMD;
import cn.navclub.xtm.kit.client.XTClient;
import cn.navclub.xtm.kit.enums.XTClientStatus;

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

    /**
     *
     * 是否监听{@link XTClient}状态变化
     *
     */
    default boolean lStatus(){
        return false;
    }

    /**
     *
     * 获取当前listener名称,默认返回当前类名称
     *
     */
    default String getName(){
        return this.getClass().getSimpleName();
    }

}
