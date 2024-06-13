package component;

import global.Config;
import global.Global;
import logic.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static global.Global.game;
import static global.Global.window;

public class Window extends JFrame {
    JMenuBar menuBar;
    JMenu fileOperationMenu, gameMenu;
    JMenuItem saveManual, resign, reckon, newGame;

    public Window() throws HeadlessException {
        initMenu();
        //this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setSize(Config.WIDTH, Config.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Global.board = new BoardComponent();
        //this.getContentPane().add(gameBoard, BorderLayout.CENTER);
        this.add(Global.board);

//        Button resignButton = new Button("认输");
//        Button endButton = new Button("结束对局并标记死子");
//        this.add(resignButton);
//        this.add(endButton);

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
            game.mode = Global.Mode.endGame;
            game.winner = (game.id % 2 == 0 ? "White" : "Black");
            String loser = (game.id % 2 == 1 ? "White" : "Black");
            String msg  = loser + " resigned\n" + game.winner + " wins";
            JOptionPane.showMessageDialog(window, msg, "result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    static class ReckoningListener implements ActionListener {
        // 对局结束的清算阶段
        @Override
        public void actionPerformed(ActionEvent e) {
            if(game.mode == Global.Mode.inGame) {
                game.mode = Global.Mode.markingDead;
                game.startMark();
            } else if(game.mode == Global.Mode.markingDead) {
                game.mode = Global.Mode.endGame;
                game.confirmMark();
                game.handleResult(Global.Rule.CN);
                String msg = "Black: " + game.sumB + "\n" +
                             "White: " + game.sumW + "\n"
                        + game.winner + " wins by " + game.diff;
                JOptionPane.showMessageDialog(window, msg, "result", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}

