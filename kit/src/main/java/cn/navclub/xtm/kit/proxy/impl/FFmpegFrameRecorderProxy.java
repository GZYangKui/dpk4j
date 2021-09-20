package cn.navclub.xtm.kit.proxy.impl;

import cn.navclub.xtm.kit.proxy.FFmpegProxy;
import org.bytedeco.javacv.Frame;

import java.util.function.Consumer;

/**
 *
 * {@link org.bytedeco.javacv.FFmpegFrameRecorder 代理类}
 *
 *
 * @author yangkui
 */
public final class FFmpegFrameRecorderProxy implements FFmpegProxy {
    @Override
    public FFmpegProxy start() {
        return null;
    }

    @Override
    public FFmpegProxy setFilename(String filename) {
        return null;
    }

    @Override
    public FFmpegProxy setFormat(String format) {
        return null;
    }

    @Override
    public FFmpegProxy setImgWidth(int width) {
        return null;
    }

    @Override
    public FFmpegProxy setImgHeight(int height) {
        return null;
    }

    @Override
    public FFmpegProxy callback(Consumer<Frame> callback) {
        return null;
    }

    @Override
    public void stop() {

    }
}
