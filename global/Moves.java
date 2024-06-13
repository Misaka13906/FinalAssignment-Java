package global;

import java.util.ArrayList;
import static global.Global.Color.*;

public class Moves extends ArrayList<Move> {
    Moves() {
        this.add(new Move());
    }

}
class Move {
    public int x, y, id, root, liberty;
    public Global.Color color;
    public boolean exist;
    Move(int x, int y, Global.Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.exist = true;

    }
    Move() {
        this.color = blank;
        this.exist = false;
    }
}
