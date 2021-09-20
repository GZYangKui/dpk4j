package cn.navclub.xtm.kit.proxy;

import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameGrabberProxy;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameRecorderProxy;
import org.bytedeco.javacv.Frame;

import java.util.function.Consumer;

public sealed interface FFmpegProxy permits FFmpegFrameRecorderProxy, FFmpegFrameGrabberProxy {
    /**
     *
     * 启动代理类
     *
     */
    FFmpegProxy start();

    /**
     *
     * 设置FFmpeg#filename属相
     *
     */
    FFmpegProxy setFilename(String filename);

    /**
     *
     * 设置FFmpeg#format属性
     *
     */
    FFmpegProxy setFormat(String format);

    /**
     *
     * 设置FFmpeg#ImageWidth属性
     *
     */
    FFmpegProxy setImgWidth(int width);

    /**
     *
     * 设置FFmpeg#ImageHeight属性
     *
     */
    FFmpegProxy setImgHeight(int height);

    /**
     *
     * 注册数据输出接收程序
     *
     */
    FFmpegProxy callback(Consumer<Frame> callback);

    /**
     *
     * 停止代理类
     *
     */
    void stop();


    static FFmpegProxy createGraProxy(){
        return new FFmpegFrameGrabberProxy();
    }


    static FFmpegProxy createRdProxy(){
        return new FFmpegFrameRecorderProxy();
    }
}
