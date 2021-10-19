package cn.navclub.xtm.kit.listener.impl;

import cn.navclub.xtm.core.decode.RecordParser;
import cn.navclub.xtm.kit.client.XClient;
import cn.navclub.xtm.kit.client.impl.TCPClient;
import cn.navclub.xtm.kit.enums.XTClientStatus;
import cn.navclub.xtm.kit.listener.XTClientListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@link  cn.navclub.xtm.kit.listener.XTClientListener} 事件分发程序
 */
public class LTDistribute {
    private static final Logger LOG = LoggerFactory.getLogger(LTDistribute.class);

    private final List<XTClientListener> listeners;


    private LTDistribute() {
        this.listeners = new ArrayList<>();
    }


    public synchronized void removeListener(XTClientListener listener) {
        Objects.requireNonNull(listener);
        if (!listeners.contains(listener)) {
            return;
        }
        listeners.remove(listener);
    }

    public synchronized void addListener(XTClientListener listener) {
        Objects.requireNonNull(listener);
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }

    /**
     * 投递TCP消息到已注册listener
     */
    public void onTPMessage(boolean udp, XClient client, RecordParser.Record record) {
        //投递消息
        for (XTClientListener listener : this.listeners) {
            //判断监听器是否监听该socket命令
            if (!listener.actions().contains(record.getCmd())) {
                continue;
            }
            Throwable ex = null;
            try {
                LOG.debug("Delivery message to {} listener", listener.getName());
                listener.onMessage(udp, client, record);
            } catch (Exception e) {
                ex = e;
            }
            if (ex != null) {
                LOG.debug("Delivery message fail.", ex);
            } else {
                LOG.debug("Success delivery message to {} listener.", listener.getName());
            }
        }
    }

    /**
     * 投递当前{@link TCPClient}状态改变信息
     */
    public void onTPStatus(boolean udp, XClient client, XTClientStatus oldStatus, XTClientStatus newStatus) {
        for (XTClientListener listener : this.listeners) {
            if (!listener.lStatus()) {
                return;
            }
            Throwable ex = null;
            try {
                listener.statusHandler(udp, client, oldStatus, newStatus);
            } catch (Exception e) {
                ex = e;
            }
            if (ex != null) {
                LOG.debug("Delivery XClient status fail", ex);
            } else {
                LOG.debug("Success delivery XClient status to {} listener.", listener.getName());
            }
        }
    }

    private static LTDistribute instance;

    public synchronized static LTDistribute getInstance() {
        if (instance == null) {
            instance = new LTDistribute();
        }
        return instance;
    }

}
