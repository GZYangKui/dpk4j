package cn.navclub.xtm.app.util;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class UIUtil {
    private static final Screen SCREEN = Screen.getPrimary();
    private static final Logger LOG = LoggerFactory.getLogger(UIUtil.class);

    /**
     * 获取屏幕尺寸
     */
    public static Rectangle2D getSrnSize() {
        return SCREEN.getBounds();
    }

    /**
     * 获取屏幕可见区域尺寸
     */
    public static Rectangle2D getVSrnSize() {
        return SCREEN.getVisualBounds();
    }

    /**
     * 异步返回用户保存节点上渲染情况到图片上
     *
     * @return 如果用户取消保存->null 保存失败->{@link Optional#empty()} 保存成功->完整保存路径
     */
    public static CompletableFuture<Optional<String>> snapNode(Node node, String filename) {
        var image = node.snapshot(null, null);
        var bi = SwingFXUtils.fromFXImage(image, null);
        var fileChooser = new FileChooser();
        fileChooser.setInitialFileName(filename);
        var file = fileChooser.showSaveDialog(null);
        if (file == null) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.supplyAsync(() -> {
            String path = null;
            try (var in = new FileOutputStream(file)) {
                var success = ImageIO.write(bi, "png", in);
                if (success) {
                    path = file.getAbsolutePath();
                }
            } catch (IOException e) {
                LOG.error("保存组件闪照失败", e);
                e.printStackTrace();
            }
            return Optional.ofNullable(path);
        });
    }
}
