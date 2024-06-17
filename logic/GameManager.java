package logic;

import java.awt.Color;
import javax.swing.*;

import global.*;
import static global.Config.*;
import static global.GlobalVal.*;
import static global.GlobalVal.BoardColor.*;

public class GameManager {
    public ResultHandler res = new ResultHandler();
    public Mode mode;
    public int id;
    public Situation[] situations;
    public Moves moves = new Moves();
    Moves m = new Moves();

    Situation tmp;
    boolean isLegal;

    public GameManager() {
        situations = new Situation[MAXMOVE];
        situations[0] = new Situation();
        situations[1] = new Situation();
        mode = Mode.inGame;
        id = 0;
    }
    public boolean placePiece(int id, int x, int y)
    {
        if(situations[id].board[x][y] != blank) {
            return false;
        }
        isLegal = true;
        tmp.set(situations[id]);
        m = (Moves) moves.clone();
        m.add(new Move(id+1, x, y));
        tmp.pieces[x][y] = id+1;
        tmp.board[x][y] = m.get(id+1).color;

        calcLiberty(tmp, id+1);
        clearPieces(id+1);

        isLegal &= id==0 || !isSame(situations[id].board);
        if(!isLegal) {
            JOptionPane.showMessageDialog(window, "" + m.get(m.get(id).root).liberty);
            return false;
        }
        copyBack(id+1);
        //board.removeAll();
        board.showBoard(situations[id+1]);
        //board.repaint();
        //showTurn(moves.get(id).color, id);
        return true;
    }

    boolean[] vis = new boolean[SIZE*SIZE+1], counted = new boolean[SIZE*SIZE+1];
    void calcLiberty(Situation tmp, int id)
    {
        //m.get(id).root = id; //并查集初始化
        for(int i=0; i<5; i++) {
            int x = m.get(id).x + dx[i], y = m.get(id).y + dy[i];
            if(!Util.inBoard(x, y) || tmp.board[x][y] == blank) {
                continue;
            }
            if(tmp.board[x][y] == m.get(id).color) {
                int an1 = findRoot(id);
                int an2 = findRoot(tmp.pieces[x][y]);
                if(an1 != an2) {
                    m.get(an1).root = an2;
                }
            }
            int rt = findRoot(tmp.pieces[x][y]);
            m.get(rt).liberty = 0;
            for(int j=0; j<SIZE*SIZE; j++)
                vis[j] = counted[j] = false;
            countLiberty(m.get(rt).x, m.get(rt).y, rt);
        }
    }

    int debugvar = 0;
    void countLiberty(int x0, int y0, int rt)
    {
        for(int i=0; i<4; i++) {
            int x = x0 + dx[i], y = y0 + dy[i];
            if(!Util.inBoard(x, y) || vis[x * SIZE + y]) {
                continue;
            }
            vis[x * SIZE + y] = true;
            if(tmp.board[x][y] == tmp.board[x0][y0]) {
                countLiberty(x, y, rt);
            } else if(tmp.board[x][y] == blank && !counted[x * SIZE + y]) {
                counted[x * SIZE + y] = true;
                m.get(rt).liberty++;
            }
            debugvar++;
        }
    }

    // findRoot: 使用并查集查找棋子所在棋串集合的唯一标识（祖先的id）
    int findRoot(int id)
    {
        if(m.get(id).root == id) {
            return id;
        }
        return m.get(id).root = findRoot(m.get(id).root);
    }

    void clearPieces(int id)
    {
        for(int i=0; i<5; i++) {
            int x = m.get(id).x + dx[i], y = m.get(id).y + dy[i];
            if(!Util.inBoard(x, y) || tmp.board[x][y] == blank) {
                continue;
            }
            int liberty = m.get(findRoot(tmp.pieces[x][y])).liberty;
            if(tmp.board[x][y] != m.get(id).color && liberty == 0) {
                clear(x, y);
            }
        }
        if(m.get(findRoot(id)).liberty == 0) {
            //clear(m[id].x, m[id].y); //Tromp-Taylor规则
            isLegal = false; //Chinese rule
        }
    }

    // clear: 使用深度优先搜索清除无气棋串
    void clear(int x, int y)
    {
        BoardColor color = tmp.board[x][y];
        if(color == blank) {
            return;
        }
        if(color == black) {
            tmp.deadBlack ++;
        } else if (color == white) {
            tmp.deadWhite ++;
        }
        tmp.board[x][y] = blank;
        m.get(tmp.pieces[x][y]).exist = false;

        int root = 0;
        for(int i=0; i<4; i++) {
            int x2 = x + dx[i], y2 = y + dy[i];
            if(!Util.inBoard(x, y) || tmp.board[x2][y2] == blank) {
                continue;
            }
            if(tmp.board[x2][y2] == color) {
                clear(x2, y2);
            } else if(findRoot(tmp.pieces[x2][y2]) != root) {
                root = m.get(tmp.pieces[x2][y2]).root;
                m.get(root).liberty ++;
            }
        }
    }

    boolean isSame(BoardColor[][] previous)
    {
        for(int i=0; i<SIZE; i++) {
            for(int j=0; j<SIZE; j++) {
                if(tmp.board[i][j] != previous[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    void copyBack(int id) {
        situations[id].set(tmp);
        moves = (Moves) m.clone();
    }




    enum operation {
        mark, cancelMark
    }
    Boolean[][] isMarked = new Boolean[SIZE][SIZE];

    public void startMark(Situation now) {
        tmp.set(now);
        for (int i=0; i<SIZE; i++)
            for(int j=0; j<SIZE; j++)
                isMarked[i][j] = false;
    }

    public void selectDead(int x, int y)
    {
        if(tmp.board[x][y] == blank) {
            return;
        }
        if(!isMarked[x][y]) {
            markDead(x, y, tmp.board[x][y], operation.mark);
        } else {
            markDead(x, y, tmp.board[x][y], operation.cancelMark);
        }
    }

    public void confirmMark(Situation now) {
        for(int i=0; i<SIZE; i++) {
            for(int j=0; j<SIZE; j++) {
                if(isMarked[i][j]) {
                    remove(i, j);
                }
            }
        }
	    now.set(tmp);
    }

    void markDead(int x, int y, BoardColor color, operation op)
    {
        if(!Util.inBoard(x, y)) {
            return;
        }
        if(tmp.board[x][y] != color && tmp.board[x][y] != blank) {
            return;
        }

        if(op == operation.mark) {
            if(isMarked[x][y]) {
                return;
            }
            isMarked[x][y] = true;
            if(tmp.board[x][y] == black) {
                tmp.deadBlack ++;
                board.drawMark(x, y, Color.white);
            } else if(tmp.board[x][y] == white) {
                tmp.deadWhite ++;
                board.drawMark(x, y, Color.black);
            }
        } else {
            if(!isMarked[x][y]) {
                return;
            }
            isMarked[x][y] = false;
            if(tmp.board[x][y] == black) {
                tmp.deadBlack --;
                board.drawMark(x, y, Color.black);
            } else if(tmp.board[x][y] == white) {
                tmp.deadWhite --;
                board.drawMark(x, y, Color.white);
            }
        }

        for(int i=0; i<4; i++) {
            markDead(x+dx[i], y+dy[i], color, op);
        }
    }

    void remove(int x0, int y0) {
        if(!moves.get(tmp.pieces[x0][y0]).exist) {
            return;
        }
        moves.get(tmp.pieces[x0][y0]).exist = false;

        for(int i=0; i<4; i++) {
            int x = x0 + dx[i], y = y0 + dy[i];
            if(!Util.inBoard(x, y)) {
                continue;
            }
            if(tmp.board[x][y] == tmp.board[x][y]) {
                remove(x, y);
            }
        }

        tmp.board[x0][y0] = blank;
    }





}

