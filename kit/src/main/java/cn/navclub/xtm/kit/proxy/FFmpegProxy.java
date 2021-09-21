package cn.navclub.xtm.kit.proxy;

import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameGrabberProxy;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameRecorderProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * 封装FFmpeg通用代理类
 *
 *
 * @author yangkui
 */
public sealed abstract class FFmpegProxy permits FFmpegFrameRecorderProxy, FFmpegFrameGrabberProxy {
    protected final Logger logger;


    private int imgWidth;
    private int imgHeight;
    private String format;
    private String filename;



    public FFmpegProxy() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    /**
     * 停止代理类
     */
    protected abstract void stop0() throws Exception;

    protected abstract void start0() throws Exception;
    /**
     * 启动代理类
     */
    public void start(){
        try {
            this.start0();
            this.logger.info("成功启动FFmpeg代理类:{}",this.getClass().getName());
        } catch (Exception e) {
            logger.error("启动FFmpeg代理类失败",e);
            throw new RuntimeException(e);
        }
    }

    public void stop(){
        try {
            this.stop0();
            this.logger.info("成功关闭FFmpeg代理类:{}",this.getClass().getName());
        } catch (Exception e) {
            logger.error("关闭FFmpeg代理类失败",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置FFmpeg#filename属相
     */
    public FFmpegProxy setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    /**
     * 设置FFmpeg#format属性
     */
    public FFmpegProxy setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * 设置FFmpeg#ImageWidth属性
     */
    public FFmpegProxy setImgWidth(int width) {
        this.imgWidth = width;
        return this;
    }

    /**
     * 设置FFmpeg#ImageHeight属性
     */
    public FFmpegProxy setImgHeight(int height) {
        this.imgHeight = height;
        return this;
    }



   public static FFmpegProxy createRdProxy() {
        return new FFmpegFrameRecorderProxy();
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
}
