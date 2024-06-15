package logic;

import java.awt.Color;

import global.Situation;
import global.Util;
import global.Global;

import static global.Config.*;
import static global.Global.*;
import static global.Global.Rule.*;
import static global.Global.Color.*;

public class HandleResult {
    Boolean[][] visit = new Boolean[SIZE][SIZE];
    public int sumB, sumW; //总目数
    int totB, totW; //棋块数
    public double diff;
    public String winner;
    Global.Rule rule;
    public void handleResult(Global.Rule goRule, Situation finalSitu)
    {
        for (int i=0; i<SIZE; i++)
            for(int j=0; j<SIZE; j++)
                visit[i][j] = false;
        sumB = sumW = totB = totW = 0;
        diff = 0;
        rule = goRule;
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