package component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import global.Config;
import logic.GameManager;

import static global.GlobalVal.*;

public class Window extends JFrame {
    JMenuBar menuBar;
    JMenu fileOperationMenu, gameMenu;
    JMenuItem saveManual, resign, reckon, newGame, info;

    public Window() throws HeadlessException {
        initMenu();
        this.setSize(Config.WIDTH, Config.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new BoardComponent();
        board.addMouseListener(new MouseActionListener());
        this.add(board);

        this.setVisible(true);
    }

    private void initMenu() {
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        fileOperationMenu = new JMenu("file");

        saveManual = new JMenuItem("save");
        saveManual.addActionListener(new SaveManualListener());

        fileOperationMenu.add(saveManual);

        gameMenu = new JMenu("game");

        resign = new JMenuItem("resign");
        resign.addActionListener(new ResignListener());
        reckon = new JMenuItem("reckon");
        reckon.addActionListener(new ReckoningListener());
        newGame = new JMenuItem("new");
        newGame.addActionListener(event -> {
            game = new GameManager();
            board.repaint();
        });

        gameMenu.add(newGame);
        gameMenu.add(resign);
        gameMenu.add(reckon);

        info = new JMenuItem("info");
        info.addActionListener(event ->
            JOptionPane.showMessageDialog(window,
                """
                    围棋对弈 v1.0
                    完成时间：
                    姓名：
                    学号：
                    班级：
                """, "info", JOptionPane.INFORMATION_MESSAGE));

        menuBar.add(fileOperationMenu);
        menuBar.add(gameMenu);
        menuBar.add(info);
    }

    private static class ResignListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.mode = Mode.endGame;
            game.res.winner = (game.id % 2 == 0 ? "White" : "Black");
            String loser = (game.id % 2 == 1 ? "White" : "Black");
            String msg  = loser + " resigned\n" + game.res.winner + " wins";
            JOptionPane.showMessageDialog(window, msg, "result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static class ReckoningListener implements ActionListener {
        // 对局结束的清算阶段
        @Override
        public void actionPerformed(ActionEvent e) {
            if(game.mode == Mode.inGame) {
                game.mode = Mode.markingDead;
                game.startMark(game.situations[game.id]);
            } else if(game.mode == Mode.markingDead) {
                board.repaint();
                game.mode = Mode.endGame;
                game.confirmMark(game.situations[game.id]);
                game.res.handleResult(Rule.CN, game.situations[game.id]);
                String msg = "Black: " + game.res.sumB + "\n" +
                             "White: " + game.res.sumW + "\n"
                        + game.res.winner + " wins by " + game.res.diff;
                JOptionPane.showMessageDialog(window, msg, "result", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}

