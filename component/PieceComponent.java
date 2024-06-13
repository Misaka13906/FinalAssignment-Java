package component;

import javax.swing.*;
import java.awt.*;

import global.Global;
import global.Util;
import static global.Config.*;

public class PieceComponent extends JPanel {
    private int x, y;
    Global.Color color;
    PieceComponent(int x, int y, Global.Color color) {
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
