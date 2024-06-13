package component;

import global.Config;

import javax.swing.*;
import java.awt.*;

import static global.Global.window;

public class Window extends JFrame {
    JMenuBar menuBar;
    JMenu fileOperationMenu, gameMenu;
    JMenuItem saveManual, resign, reckon;

    public Window() throws HeadlessException {
        initMenu();
        //this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setSize(Config.WIDTH, Config.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BoardComponent gameBoard = new BoardComponent();
        this.getContentPane().add(gameBoard, BorderLayout.CENTER);
        //this.add(gameBoard);

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

        reckon = new JMenuItem("reckon");
        reckon.addActionListener(new ReckoningListener());
        gameMenu.add(reckon);

        menuBar.add(fileOperationMenu);
        menuBar.add(gameMenu);

    }
}

