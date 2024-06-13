package component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import global.Config;
import global.Util;

import static global.Global.Color.*;
import static global.Global.*;

class MouseActionListener implements MouseListener, MouseMotionListener {
    JPanel board;
    public MouseActionListener(JPanel board) {
        this.board = board;
    }

    @Override
    public void mouseClicked(MouseEvent e) { //落子
        int x = Util.getCoordinate(e.getX());
        int y = Util.getCoordinate(e.getY());
        if(!Util.inBoard(x, y)) {
            return;
        }
        //JOptionPane.showMessageDialog(window, x + ", " + y);
        //board.add(new PieceComponent(x, y, black));
        //window.validate();
//        if(Global.mode == Global.Mode.inGame) {
//            boolean success = placePiece(x, y, id+1, prev);
//            if(success) {
//                id++;
//            } else {
//                JOptionPane.showMessageDialog(window, "不正确的落子");
//            }
//        } else if(Global.mode == Global.Mode.markingDead) {
//            selectDead(x, y);
//        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {  //用方块显示鼠标所在位置
        int x = Util.getCoordinate(e.getX());
        int y = Util.getCoordinate(e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }

    @Override  public void mouseDragged(MouseEvent e) {}
    @Override  public void mousePressed(MouseEvent e) {}
    @Override  public void mouseReleased(MouseEvent e) {}
}
