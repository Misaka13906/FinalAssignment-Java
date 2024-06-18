package component;

import javax.swing.*;
import java.awt.event.*;

import global.Util;

import static global.GlobalVal.*;

class MouseActionListener implements MouseListener, MouseMotionListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = Util.getCoordinate(e.getX());
        int y = Util.getCoordinate(e.getY());
        if(!Util.inBoard(x, y)) {
            return;
        }
        //board.add(new PieceComponent(x, y, black));
        if(game.mode == Mode.inGame) {
            boolean success = game.placePiece(game.id, x, y); //落子
            if(success) {
                game.id++;
                board.repaint();
            } else {
                JOptionPane.showMessageDialog(window, "不正确的落子");
            }
        } else if(game.mode == Mode.markingDead) {
            game.selectDead(x, y);
            board.repaint();
        }
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
