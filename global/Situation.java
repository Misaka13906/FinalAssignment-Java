package global;


public class Situation {
    static final int SIZE = Config.SIZE;
    public int id = 0, deadBlack = 0, deadWhite = 0;
    public int[][] pieces = new int[SIZE][SIZE];
    public Global.Color[][] board = new Global.Color[SIZE][SIZE];

    public Situation() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = Global.Color.blank;
            }
        }
    }

    public Situation copy() {
        Situation s = new Situation();
        s.id = id;
        s.deadBlack = deadBlack;
        s.deadWhite = deadWhite;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                s.pieces[i][j] = pieces[i][j];
                s.board[i][j] = board[i][j];
            }
        }
        return s;
    }
}
