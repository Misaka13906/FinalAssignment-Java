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
    JMenuItem saveManual, resign, reckon, newGame;

    public Window() throws HeadlessException {
        initMenu();
        this.setSize(Config.WIDTH, Config.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new BoardComponent();
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

        newGame = new JMenuItem("new");
        newGame.addActionListener(new RestartListener());
        gameMenu.add(newGame);

        resign = new JMenuItem("resign");
        resign.addActionListener(new ResignListener());
        gameMenu.add(resign);

        reckon = new JMenuItem("reckon");
        reckon.addActionListener(new ReckoningListener());
        gameMenu.add(reckon);

        menuBar.add(fileOperationMenu);
        menuBar.add(gameMenu);

    }

    static class RestartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game = new GameManager();
        }
    }

    static class ResignListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.mode = Mode.endGame;
            game.res.winner = (game.id % 2 == 0 ? "White" : "Black");
            String loser = (game.id % 2 == 1 ? "White" : "Black");
            String msg  = loser + " resigned\n" + game.res.winner + " wins";
            JOptionPane.showMessageDialog(window, msg, "result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    static class ReckoningListener implements ActionListener {
        // 对局结束的清算阶段
        @Override
        public void actionPerformed(ActionEvent e) {
            if(game.mode == Mode.inGame) {
                game.mode = Mode.markingDead;
                game.startMark(game.situations[game.id]);
            } else if(game.mode == Mode.markingDead) {
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

