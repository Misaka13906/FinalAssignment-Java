package component;

import javax.swing.*;
import java.awt.*;

import global.Situation;
import global.Util;
import static global.Config.*;
import static global.GlobalVal.*;
import static global.GlobalVal.BoardColor.*;

public class BoardComponent extends JPanel {
    Graphics g;
    BoardComponent() {
        super();
        //this.setLayout(null);
        MouseActionListener mouselistener = new MouseActionListener(this);
        this.addMouseListener(mouselistener);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(PANEL, PANEL));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.g = g;
        drawBoard();
        //showBoard(game.situations[game.id]);

        System.out.println(g);
        //drawPiece(3,2, Global.Color.black);
    }

    public void drawBoard() {
        g.setColor(Color.yellow);
        g.clearRect(MARGIN, MARGIN, BOARD, BOARD);
        g.fillRect(MARGIN, MARGIN, BOARD, BOARD);

        g.setColor(Color.black);

        //draw lines
        for(int i=0; i<SIZE; i++) {
            g.drawLine(SIDE + i*CELL, SIDE, SIDE + i*CELL, PANEL - SIDE);
            g.drawLine(SIDE, SIDE + i*CELL, PANEL - SIDE, SIDE + i*CELL);
        }

        //draw dots
        int step = (SIZE-7)/2, start = 3;
        if(step<start) {
            step = SIZE-7;
        }
        if(step<start) {
            start = 2;
        }
        for(int i=start; i<SIZE-start; i+=step) {
            for(int j=start; j<SIZE-start; j+=step) {
                Util.fillCircle(g, SIDE + i*CELL, SIDE + j*CELL, 2);
            }
        }
    }

    public void drawPiece(int x, int y, BoardColor color) {
        if(color == black) g.setColor(Color.black); else
        if(color == white) g.setColor(Color.white); else return;
        Util.fillCircle(g, SIDE + x*CELL, SIDE + y*CELL, RADIUS);
        //System.out.println(g);
        //g.fillArc(SIDE+x*CELL - RADIUS, SIDE+y*CELL - RADIUS, RADIUS*2, RADIUS*2, 0, 360);
        //JOptionPane.showMessageDialog(window, x + ", " + y);
    }

    public void showBoard(Situation now) {
        //this.removeAll();
        this.repaint();

        debugCounter++;
        System.out.println(debugCounter);

        drawBoard();
        for(int i=0; i<SIZE; i++) {
            for(int j=0; j<SIZE; j++) {
                if(now.board[i][j] != blank) {
                    drawPiece(i, j, now.board[i][j]);
                }
            }
        }
    }

    public void drawMark(int x, int y, Color color)
    {
        x = Util.getPosition(x);
        y = Util.getPosition(y);
        g.setColor(color);
        g.drawRect(x - SQUARE/2, y - SQUARE/2, SQUARE, SQUARE);
    }
}