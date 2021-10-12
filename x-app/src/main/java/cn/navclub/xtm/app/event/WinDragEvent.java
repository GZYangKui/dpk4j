package cn.navclub.xtm.app.event;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class WinDragEvent {
    private final Stage stage;

    private WinDragEvent(Stage stage, Node node) {
        this.stage = stage;
        node.addEventFilter(MouseEvent.ANY,this::eventFilter);
    }

    private double x;
    private double y;


    private void eventFilter(MouseEvent event){

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED){
            this.x = event.getSceneX();
            this.y = event.getSceneY();
        }


        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED){
            var xx = event.getScreenX();
            var yy = event.getScreenY();

            var a = xx-this.x;
            var b = yy-this.y;

            this.stage.setX(a);
            this.stage.setY(b);
        }
    }



    public static WinDragEvent register(Stage stage,Node node){
        return new WinDragEvent(stage,node);
    }
}