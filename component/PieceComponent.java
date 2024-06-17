package component;

import javax.swing.*;
import java.awt.*;

import global.Util;
import static global.Config.*;
import static global.GlobalVal.*;

public class PieceComponent extends JPanel {
    private int x, y;
    BoardColor color;
    PieceComponent(int x, int y, BoardColor color) {
        this.x = Util.getPosition(x);
        this.y = Util.getPosition(x);
        this.color = color;
        this.setBounds(x, y, RADIUS*2, RADIUS*2);
        this.setVisible(true);
    }
    @Override
    public void paintComponent(Graphics g){

    }


}
