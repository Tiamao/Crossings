package checkers;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * Created by Kamil on 09.04.2017.
 */
public class Piece extends StackPane {

    private int tileSize = CheckersApp.TILE_SIZE;
    private PieceType type;
    private double mouseX, mouseY;
    private double oldX, oldY;
    private CheckBox check;

    public Piece(PieceType type, int x, int y) {
        this.type = type;

        move(x, y);

        Ellipse bg = new Ellipse(tileSize * 0.3125, tileSize * 0.26); // backgroud
        bg.setFill(Color.BLACK);
        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(tileSize * 0.03);

        bg.setTranslateX((tileSize - tileSize * 0.3125 * 2) / 2);
        bg.setTranslateY((tileSize - tileSize * 0.26 * 2) / 2 + tileSize * 0.07);


        Ellipse ellipse = new Ellipse(tileSize * 0.3125, tileSize * 0.26); // backgroud
        ellipse.setFill(type == PieceType.RED ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(tileSize * 0.03);

        ellipse.setTranslateX((tileSize - tileSize * 0.3125 * 2) / 2);
        ellipse.setTranslateY((tileSize - tileSize * 0.26 * 2) / 2);

        check = new CheckBox();

        getChildren().addAll(bg, ellipse, check);
        setOnMousePressed(e->{
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e->{
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void move(int x, int y){
        oldX = x * tileSize;
        oldY = y * tileSize;
        relocate(oldX,oldY);
    }

    public void abortMove(){
        relocate(oldX, oldY);
    }

    public PieceType getType() {
        return type;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }
    public Piece getPiece(){
        return this;
    }

    public CheckBox getCheck() {
        return check;
    }
}
