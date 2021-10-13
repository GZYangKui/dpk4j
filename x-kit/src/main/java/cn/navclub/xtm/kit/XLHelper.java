package cn.navclub.xtm.kit;

import cn.navclub.xtm.kit.listener.XTClientListener;
import cn.navclub.xtm.kit.listener.impl.LTDistribute;

/**
 * {@link XTClientListener} helper
 */
public class XLHelper {
    /**
     * 移除监听器
     */
    public static synchronized void removeListener(XTClientListener listener) {
        LTDistribute.getInstance().removeListener(listener);
    }

    /**
     * 注册监听器
     */
    public static synchronized void addListener(XTClientListener listener) {
        LTDistribute.getInstance().addListener(listener);
    }
}
