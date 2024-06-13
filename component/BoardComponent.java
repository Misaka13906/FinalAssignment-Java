package component;

import javax.swing.*;
import java.awt.*;

import global.Config;
import global.Util;
import static global.Config.*;

public class BoardComponent extends JPanel {

    BoardComponent() {
        super();
        this.setLayout(null);
        MouseActionListener mouselistener = new MouseActionListener(this);
        this.addMouseListener(mouselistener);
        this.setVisible(true);
        //this.setMinimumSize(new Dimension(PANEL, PANEL));
    }

    @Override
    public void paintComponent(Graphics g){
        drawBoard(g);
    }

    public void drawBoard(Graphics g) {
        g.setColor(Color.yellow);
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


}