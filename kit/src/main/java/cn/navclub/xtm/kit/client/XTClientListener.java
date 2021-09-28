package cn.navclub.xtm.kit.client;

public interface XTClientListener {
    /**
     * {@link  XTClient} 状态发生改变回调此方法
     */
    default void statusHandler(XTClientStatus oldStatus, XTClientStatus newStatus){

    }

    /**
     * 连接发生异常触发该回调
     */
    default void exceptionHandle(Throwable e){

    }

}
