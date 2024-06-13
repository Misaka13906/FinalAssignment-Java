package logic;

import java.awt.Color;

import global.*;

import javax.swing.*;

import static global.Global.Color.*;
import static global.Global.Rule.*;
import static global.Global.*;
import static global.Config.*;

public class GameManager {
    public Mode mode;
    Situation tmp;
    boolean isLegal;

    public int id;
    public Situation[] situations;
    public Moves moves = new Moves(), m = new Moves();
    public GameManager() {
        situations = new Situation[MAXMOVE];
        situations[0] = new Situation();
        mode = Mode.inGame;
        id = 0;
    }
    public boolean placePiece(int id, int x, int y)
    {
        if(situations[id].board[x][y] != blank) {
            return false;
        }
        isLegal = true;
        tmp = situations[id].copy();
        m = (Moves) moves.clone();
        m.add(new Move(id, x, y));
        tmp.pieces[x][y] = id;
        tmp.board[x][y] = m.get(id).color;

        calcLiberty(tmp, id);
        clearPieces(id);

        isLegal &= !isSame(situations[id].board);
//        if(!isLegal) {
//            JOptionPane.showMessageDialog(window, "" + m.get(m.get(id).root).liberty);
//            return false;
//        }
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
        Global.Color color = tmp.board[x][y];
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

    boolean isSame(Global.Color[][] previous)
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
        situations[id] = tmp.copy();
        moves = (Moves) m.clone();
    }




    enum operation {
        mark, cancelMark
    }
    Boolean[][] isMarked = new Boolean[SIZE][SIZE];

    public void startMark() {
        tmp = situations[id].copy();
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

    public void confirmMark() {
        for(int i=0; i<SIZE; i++) {
            for(int j=0; j<SIZE; j++) {
                if(isMarked[i][j]) {
                    remove(i, j);
                }
            }
        }
	    situations[id] = tmp.copy();
    }

    void markDead(int x, int y, Global.Color color, operation op)
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



    Boolean[][] visit = new Boolean[SIZE][SIZE];
    public int sumB, sumW; //总目数
    int totB, totW; //棋块数
    public double diff;
    public String winner;
    Rule rule;
    public void handleResult(Rule goRule)
    {
        for (int i=0; i<SIZE; i++)
            for(int j=0; j<SIZE; j++)
                visit[i][j] = false;
        sumB = sumW = totB = totW = 0;
        diff = 0;
        rule = goRule;
        Situation finalSitu = situations[id];
        countResult(finalSitu.board);
        calResult(finalSitu.deadBlack, finalSitu.deadWhite);
        //showResult(winner, diff);
    }

    void countResult(Global.Color[][] board)
    {
        for(int i=0; i<SIZE; i++) {
            for(int j=0; j<SIZE; j++) {
                if(visit[i][j] || board[i][j] == blank) {
                    continue;
                }
                if(board[i][j] == black) {
                    totB++;
                } else {
                    totW++;
                }
                search(i, j, board[i][j], board);
            }
        }
    }

    // 使用深度优先搜索统计一块棋所占目数
    void search(int x, int y, Global.Color color, Global.Color[][] brd)
    {
        visit[x][y] = true;
        count(color, brd[x][y]);
        if(brd[x][y] == blank) {
            board.drawMark(x, y, (color == black ? Color.black : Color.white));
        }

        for(int i=0; i<4; i++) {
            int x2 = x + dx[i], y2 = y + dy[i];
            if(!Util.inBoard(x2, y2) || visit[x2][y2]) {
                continue;
            }
            if(brd[x2][y2] == color || brd[x2][y2] == blank) {
                search(x2, y2, color, brd);
            }
        }
    }

    void count(Global.Color color, Global.Color now)
    {
        if(rule == JP && now != blank) {
            return;  //Japanese rule, only count blank points
        }
        if(color == black) {
            sumB++;
        } else if(color == white) {
            sumW++;
        }
    }

    void calResult(int deadB, int deadW)
    {
        if(rule == CN) { // using Chinese rule, do not count dead pieces
            diff = sumB - sumW - 7.5;
        } else if(rule == JP) {
            diff = (sumB + deadW) - (sumW + deadB) - 6.5;
        }

        if(diff > 0) {
            winner = "black";
        } else {
            winner = "white";
            diff = - diff;
        }
    }

}
