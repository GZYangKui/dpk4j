package cn.navclub.xtm.app.controller;

import cn.navclub.xtm.app.base.AbstractWindowFXMLController;
import cn.navclub.xtm.app.config.XTApp;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameGrabberProxy;
import cn.navclub.xtm.kit.proxy.impl.FFmpegFrameRecorderProxy;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;

public class WinGrabController extends AbstractWindowFXMLController<HBox> {
    private final FFmpegFrameGrabberProxy fProxy;

    private final FFmpegFrameRecorderProxy fRecord;

    public WinGrabController(final Integer robotId) {
        super("WinGrabView.fxml");
        var rect = Screen.getPrimary().getBounds();
        this.fRecord = FFmpegFrameRecorderProxy.createProxy();

        var filename = String.format("rtmp://%s/myapp?robot=%d", XTApp.getInstance().getHost(), robotId);
        this.fRecord
                .setFilename(filename)
                .setFormat("flv")
                .setImgWidth((int) rect.getWidth())
                .setImgHeight((int) rect.getHeight())
                .start();

        //初始化FFmpeg
        this.fProxy = FFmpegFrameGrabberProxy.createGraProxy();
        var screenID = getScreenID();
        this.fProxy
                .setCallback(this.fRecord::push)
                .setFilename(String.format(":%d+%d",screenID,0))
                .setImgWidth((int) rect.getWidth())
                .setImgHeight((int) rect.getHeight())
                .setFormat("x11grab")
                .start();
    }

    @Override
    public void onRequestClose(WindowEvent event) {
        super.onRequestClose(event);
        this.fRecord.stop();
        this.fProxy.stop();
        MainViewController.newInstance().openWindow();
    }

    /**
     *
     * 临时方法 仅适合linux桌面环境
     *
     */
    public Integer getScreenID(){
        var map = System.getenv();
        var str = map.get("DISPLAY");
        if (str.startsWith(":")){
            str = str.substring(1);
        }
        return Integer.parseInt(str);
    }
}
