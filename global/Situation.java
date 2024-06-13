package global;


public class Situation {
    static final int SIZE = Config.SIZE;
    int id = 0;
    int[][] pieces = new int[SIZE][SIZE];
    public Global.Color[][] board = new Global.Color[SIZE][SIZE];

    Situation copy() {
        Situation s = new Situation();
        s.id = id;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                s.pieces[i][j] = pieces[i][j];
                s.board[i][j] = board[i][j];
            }
        }
        return s;
    }
}
