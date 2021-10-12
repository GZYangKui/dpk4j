package cn.navclub.xtm.kit.proxy.impl;


import cn.navclub.xtm.kit.proxy.FFmpegProxy;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 自定义{@link FFmpegFrameGrabber}代理类
 *
 * @author yangkui
 */
public final class FFmpegFrameGrabberProxy extends FFmpegProxy {
    private Consumer<Frame> callback;

    private FFmpegFrameGrabber grabber;

    private final FFmpegProxyThread proxyThread;


    public FFmpegFrameGrabberProxy() {
        this.proxyThread = new FFmpegProxyThread(this);
    }

    @Override
    protected void start0() throws Exception {
        Objects.requireNonNull(this.getFormat());
        Objects.requireNonNull(this.getFilename());

        this.grabber = new FFmpegFrameGrabber(this.getFilename());
        this.grabber.setFormat(this.getFormat());
        this.grabber.setImageWidth(this.getImgWidth());
        this.grabber.setImageHeight(this.getImgHeight());


        this.grabber.setFrameNumber(this.getFrameNumber());
        this.grabber.start(true);

        this.proxyThread.start();
    }

    public FFmpegProxy setCallback(Consumer<Frame> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 停止{@link FFmpegFrameGrabber}抓取屏幕信息
     */
    @Override
    public void stop0() throws Exception {
        this.proxyThread.stopGra();
        this.grabber.stop();
    }

    public Consumer<Frame> getCallback() {
        return callback;
    }

    public static FFmpegFrameGrabberProxy createGraProxy() {
        return new FFmpegFrameGrabberProxy();
    }

    /**
     * 封装线程用于异步执行获取屏幕抓取信息
     */
    private static class FFmpegProxyThread extends Thread {
        private final FFmpegFrameGrabberProxy proxy;

        private volatile boolean running;

        public FFmpegProxyThread(FFmpegFrameGrabberProxy proxy) {
            this.proxy = proxy;
            this.setName("FFFGrabber-thread");
        }

        @Override
        public synchronized void start() {
            this.running = true;
            super.start();
        }

        public void stopGra(){
            this.running = false;
        }

        @Override
        public void run() {
            while (this.running) {
                try {
                    var frame = proxy.grabber.grabFrame();
                    this.callback(frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void callback(Frame frame) {
            var callback = this.proxy.getCallback();
            if (callback == null) {
                return;
            }
            try {

                callback.accept(frame);
            } catch (Exception e) {
                //NO to do
                e.printStackTrace();
            }
        }
    }

}
