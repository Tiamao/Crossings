package checkers;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CheckersApp extends Application {

    //6:01
    private boolean move = true;
    public static final int TILE_SIZE = 100; //px
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] gameBoard = new Tile[WIDTH][HEIGHT];

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    private List<Piece> piecesList;
    private List<Piece> checkedPieces;

    private Parent createContent() {
        GridPane root = new GridPane();
        root.setPrefSize(800, 800);
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
        checkedPieces = new ArrayList<>();
        piecesList = new ArrayList<>();

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
//TODO operowac na liscie zaznaczonych
            MoveResult result = tryMove(piece,newX,newY);

            int x0 = toGameBoard(piece.getOldX());
            int y0 = toGameBoard(piece.getOldY());

            //TODO operacje wykonywac dla kazdego zaznaczonego
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
        piecesList.add(piece);
        return piece;
    }

    private int toGameBoard(double pixel){
        return (int)(pixel + TILE_SIZE/2)/TILE_SIZE;
    }

    private MoveResult tryMove(Piece piece, int newX, int newY){
// TODO złe newX i newY
        //TODO usunac ruch w mmsc gdzie jest pionek
        //TODO enum z ruchami w 3 pola i rozpoznawac pola dla kazdego pionka
//        List<MoveResult> moveResults = new ArrayList<>();
//        piecesList.forEach( e-> {
//            if(e.getCheck().isSelected()){
//                checkedPieces.add(e);
//            }
//        });
//
//        checkedPieces.forEach(e->{
//            if(gameBoard[newX][newY].hasPiece()){
//                moveResults.add(new MoveResult(MoveType.NONE));
//            }
//
//            int x0 = toGameBoard(e.getPiece().getOldX());
//            int y0 = toGameBoard(e.getPiece().getOldY());
//            //22:22
//
//            if((newY - y0) == e.getPiece().getType().moveDir || newY == y0){
//                moveResults.add(new MoveResult(MoveType.NORMAL));
//            } else if(Math.abs(newX - x0) == 2 && (newY - y0) == e.getPiece().getType().moveDir * 2){
//
//                int x1 = x0 +(newX - x0)/2;
//                int y1 = y0 +(newY - y0)/2;
//
//                if(gameBoard[x1][y1].hasPiece() && gameBoard[x1][y1].getPiece().getType() != e.getPiece().getType()){
//                    moveResults.add(new MoveResult(MoveType.KILL, gameBoard[x1][y1].getPiece()));
//                }
//            }
//            moveResults.add(new MoveResult(MoveType.NONE));
//        });
//
//        moveResults.forEach( e->{
//            if( e.getType() == MoveType.NONE){
//                move = false;
//            }
//        });
//
//        if(move){
//            return new MoveResult(MoveType.NORMAL);
//        } else {
//            return new MoveResult(MoveType.NONE);
//        }

        if(gameBoard[newX][newY].hasPiece()){
            return new MoveResult(MoveType.NONE);
        }

        int x0 = toGameBoard(piece.getOldX());
        int y0 = toGameBoard(piece.getOldY());
        //22:22

        if((newY - y0) == piece.getType().moveDir || newY == y0){
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