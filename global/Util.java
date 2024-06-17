package global;

import java.awt.*;

import static global.Config.*;

public class Util {

    public static int getCoordinate(int x) {
        return (x - SIDE + CELL/2) / CELL;
    }
    public static int getPosition(int x) {
        return x * CELL + SIDE;
    }
    public static void fillCircle(Graphics g, int x, int y, int r) {
        g.fillArc(x - r, y - r, r*2, r*2, 0, 360);
    }

    public static boolean inBoard(int x, int y) {
        return x>=0 && y>=0 && x<SIZE && y<SIZE;
    }
}

