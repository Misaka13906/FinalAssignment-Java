package global;

import static global.GlobalVal.BoardColor;
import static global.GlobalVal.BoardColor.*;

public class Move {
    public int x, y, id, root, liberty;
    public BoardColor color;
    public boolean exist;
    public Move(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = (id%2 == 1 ? black : white);
        this.exist = true;
        this.root = id; // 并查集初始化
    }
    Move() {
        this.color = blank;
        this.exist = false;
    }

    public Move copy() {
        Move m = new Move();
        m.x = x;
        m.y = y;
        m.color = color;
        m.root = root;
        m.liberty = liberty;
        m.exist = exist;
        return m;
    }
}
