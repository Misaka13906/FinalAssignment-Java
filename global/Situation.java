package global;

import static global.GlobalVal.BoardColor;
import static global.GlobalVal.BoardColor.*;

public class Situation {
    static final int SIZE = Config.SIZE;
    public int id = 0, deadBlack = 0, deadWhite = 0;
    public int[][] pieces = new int[SIZE][SIZE];
    public BoardColor[][] board = new BoardColor[SIZE][SIZE];

    public Situation() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = blank;
            }
        }
    }

    public void set(Situation s) {
        this.id = s.id;
        this.deadBlack = s.deadBlack;
        this.deadWhite = s.deadWhite;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.pieces[i][j] = s.pieces[i][j];
                this.board[i][j] = s.board[i][j];
            }
        }
    }
}
