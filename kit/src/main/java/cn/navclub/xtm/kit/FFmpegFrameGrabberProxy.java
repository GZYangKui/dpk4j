package cn.navclub.xtm.kit;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;

public class FFmpegFrameGrabberProxy {
    private static final Logger LOG = LoggerFactory.getLogger(FFmpegFrameGrabberProxy.class);


    private int imgWidth;
    private int imgHeight;
    private String format;
    private String filename;

    private Consumer<Frame> consumer;


    private FFmpegFrameGrabber grabber;


    private final FFFGrabberThread grabberThread;

    private volatile boolean running;

    public FFmpegFrameGrabberProxy() {
        this.running = false;
        this.grabberThread = new FFFGrabberThread(this);
    }

    public FFmpegFrameGrabberProxy start() {
        Objects.requireNonNull(this.format);
        Objects.requireNonNull(this.filename);

        this.grabber = new FFmpegFrameGrabber(this.filename);
        this.grabber.setFormat(this.format);
        this.grabber.setImageWidth(this.imgWidth);
        this.grabber.setImageHeight(this.imgHeight);

        try {
            this.grabber.start(true);
            this.grabberThread.start();
            this.running = true;
        } catch (FFmpegFrameGrabber.Exception e) {
            LOG.error("启动FFmpegFrameGrabber失败", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * 停止{@link FFmpegFrameGrabber}抓取屏幕信息
     */
    public void stop() {
        try {
            this.running = false;
            this.grabber.stop();
        } catch (FFmpegFrameGrabber.Exception e) {
            LOG.error("停止FFmpegFrameGrabber失败",e);
        }
    }


    public static FFmpegFrameGrabberProxy createProxy() {
        return new FFmpegFrameGrabberProxy();
    }

    /**
     * 封装线程用于异步执行获取屏幕抓取信息
     */
    private static class FFFGrabberThread extends Thread {
        private final FFmpegFrameGrabberProxy proxy;

        public FFFGrabberThread(FFmpegFrameGrabberProxy proxy) {
            this.proxy = proxy;
            this.setName("FFFGrabber-thread");
        }

        @Override
        public void run() {
            while (proxy.running) {
                try {
                    var frame = proxy.grabber.grabFrame();
                    if (proxy.consumer != null) {
                        proxy.consumer.accept(frame);
                    }
                } catch (FrameGrabber.Exception e) {
                    LOG.error("获取Frame数据失败", e);
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public FFmpegFrameGrabberProxy setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
        return this;
    }

    public FFmpegFrameGrabberProxy setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
        return this;
    }

    public FFmpegFrameGrabberProxy setConsumer(Consumer<Frame> consumer) {
        this.consumer = consumer;
        return this;
    }

    public FFmpegFrameGrabberProxy setFormat(String format) {
        this.format = format;
        return this;
    }

    public FFmpegFrameGrabberProxy setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public String getFormat() {
        return format;
    }

    public String getFilename() {
        return filename;
    }

    public Consumer<Frame> getConsumer() {
        return consumer;
    }
}
