package checkers;

/**
 * Created by Kamil on 20.05.2017.
 */
public enum NormalMoveType {

    LEFT(-1,1),
    STRAIGHT(0,1),
    RIGHT(1,1);

    int x;
    int y;

    NormalMoveType(int x , int y){
        this.x = x;
        this.y = y;
    }
}
