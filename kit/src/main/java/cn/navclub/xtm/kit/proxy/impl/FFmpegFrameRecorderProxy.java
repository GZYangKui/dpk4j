package cn.navclub.xtm.kit.proxy.impl;

import cn.navclub.xtm.kit.proxy.FFmpegProxy;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.util.Objects;

/**
 *
 * {@link org.bytedeco.javacv.FFmpegFrameRecorder 代理类}
 *
 *
 * @author yangkui
 */
public final class FFmpegFrameRecorderProxy extends FFmpegProxy {
    private FFmpegFrameRecorder recorder;

    @Override
    protected void start0() throws Exception {
        Objects.requireNonNull(this.getFormat());
        Objects.requireNonNull(this.getFilename());

        recorder = new FFmpegFrameRecorder(
                this.getFilename(),
                this.getImgWidth(),
                this.getImgHeight()
        );
        recorder.setFormat(this.getFormat());
        recorder.start();
    }

    @Override
    protected void stop0() throws Exception {
        recorder.stop();
    }

    /**
     *
     * 向目标地址推流
     *
     */
    public void push(Frame frame){
        try {
            this.recorder.record(frame);
        } catch (FFmpegFrameRecorder.Exception e) {
            this.logger.error("推流失败",e);
        }
    }


    public static FFmpegFrameRecorderProxy createProxy(){
        return new FFmpegFrameRecorderProxy();
    }
}
