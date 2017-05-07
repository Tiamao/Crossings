package checkers;

/**
 * Created by Kamil on 07.05.2017.
 */
public enum PieceType {
    RED(1),
    WHITE(-1);

    final int moveDir;

    PieceType(int moveDir){
        this.moveDir = moveDir;
    }
}
