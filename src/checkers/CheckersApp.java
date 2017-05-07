package checkers;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CheckersApp extends Application {

    //6:01

    public static final int TILE_SIZE = 100; //px
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] gameBoard = new Tile[WIDTH][HEIGHT];

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    private Parent createContent() {
        GridPane root = new GridPane();
        root.setPrefSize(1000, 1000);
        root.setAlignment(Pos.CENTER);

        Pane board = new Pane();
        board.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        board.getChildren().addAll(tileGroup, pieceGroup);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                gameBoard[x][y] = tile;

                tileGroup.getChildren().add(tile);
                Piece piece = null;
                if (y < 2 ) {
                    piece = makePiece(PieceType.RED, x, y);
                }

                if (y > 5) {
                    piece = makePiece(PieceType.WHITE, x, y);
                }

                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);

                }
            }
        }

        root.getChildren().add(board);

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.getStylesheets().add(getClass().getResource("resources/root.css").toExternalForm());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Piece makePiece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);
        piece.setOnMouseReleased(e->{
            int newX = toGameBoard(piece.getLayoutX());
            int newY = toGameBoard(piece.getLayoutY());

            MoveResult result = tryMove(piece,newX,newY);

            int x0 = toGameBoard(piece.getOldX());
            int y0 = toGameBoard(piece.getOldY());

            switch (result.getType()) {
                case NONE:
                    piece.abortMove();
                    break;
                case NORMAL:
                    piece.move(newX, newY);
                    gameBoard[x0][y0].setPiece(null);
                    gameBoard[newX][newY].setPiece(piece);
                    break;
                case KILL:
                    piece.move(newX, newY);
                    gameBoard[x0][y0].setPiece(null);
                    gameBoard[newX][newY].setPiece(piece);

                    Piece otherPiece = result.getPiece();
                    gameBoard[toGameBoard(otherPiece.getOldX())][toGameBoard(otherPiece.getOldY())].setPiece(null);
                    pieceGroup.getChildren().remove(otherPiece);
                    break;
            }
        });

        return piece;
    }

    private int toGameBoard(double pixel){
        return (int)(pixel + TILE_SIZE/2)/TILE_SIZE;
    }

    private MoveResult tryMove(Piece piece, int newX, int newY){
        if(gameBoard[newX][newY].hasPiece()){
            return new MoveResult(MoveType.NONE);
        }

        int x0 = toGameBoard(piece.getOldX());
        int y0 = toGameBoard(piece.getOldY());
        //22:22

        if(Math.abs(newX - x0) == 1 && (newY - y0) == piece.getType().moveDir){
            return new MoveResult(MoveType.NORMAL);
        } else if(Math.abs(newX - x0) == 2 && (newY - y0) == piece.getType().moveDir * 2){

            int x1 = x0 +(newX - x0)/2;
            int y1 = y0 +(newY - y0)/2;

            if(gameBoard[x1][y1].hasPiece() && gameBoard[x1][y1].getPiece().getType() != piece.getType()){
                return new MoveResult(MoveType.KILL, gameBoard[x1][y1].getPiece());
            }
        }
        return new MoveResult(MoveType.NONE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}