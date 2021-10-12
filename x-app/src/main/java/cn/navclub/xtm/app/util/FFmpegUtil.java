package cn.navclub.xtm.app.util;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;


import java.nio.ByteBuffer;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2BGRA;

public class FFmpegUtil {
    private static final Mat javaCVMat = new Mat();

    private static final OpenCVFrameConverter.ToMat javaCVConv = new OpenCVFrameConverter.ToMat();

    private static final WritablePixelFormat<ByteBuffer> formatByte = PixelFormat.getByteBgraPreInstance();

    public static WritableImage toFXImage(Frame frame){
        var w = frame.imageWidth;
        var h = frame.imageHeight;
        var mat = javaCVConv.convert(frame);
        opencv_imgproc.cvtColor(mat, javaCVMat, COLOR_BGR2BGRA);

        var buffer = javaCVMat.createBuffer();

        var pixelBuffer = new PixelBuffer(w, h, buffer, formatByte);

        return new WritableImage(pixelBuffer);
    }
}
