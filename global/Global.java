package global;

import component.BoardComponent;
import component.Window;
import logic.GameManager;

public class Global {
    public static final int[] dx = {0,1,0,-1,0};
    public static final int[] dy = {1,0,-1,0,0};
    public static final int MAXMOVE = 1000;
    public enum Mode {
        inGame, markingDead, endGame
    };

    public enum Color {
        blank, white, black
    }

    public enum Rule {
        CN, JP
    }

    public static Window window;
    public static BoardComponent board;
    public static GameManager game;
}
