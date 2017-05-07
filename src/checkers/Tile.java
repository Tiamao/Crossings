package checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kamil on 09.04.2017.
 */
public class Tile extends Rectangle {

    private Piece piece;

    public Tile(boolean light, int x, int y) {
        setWidth(CheckersApp.TILE_SIZE);
        setHeight(CheckersApp.TILE_SIZE);

        relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);
        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582"));
    }

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
