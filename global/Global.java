package global;

import component.Window;

public class Global {
    public static final int[] dx = {0,1,0,-1,0};
    public static final int[] dy = {1,0,-1,0,0};
    public static final int MAXMOVE = 1000;
    public enum Mode {
        inGame, markingDead, endGame
    };
    public static Mode mode;

    public enum Color {
        blank, white, black
    }

    public static int id = 0;
    public static Situation[] situations = new Situation[MAXMOVE];
    public static Moves moves = new Moves(), m = new Moves();

    public static Window window;
}
