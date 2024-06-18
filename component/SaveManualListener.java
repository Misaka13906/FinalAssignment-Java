package component;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.*;

import static global.Config.SIZE;
import static global.GlobalVal.*;
import static global.GlobalVal.BoardColor.*;

class SaveManualListener implements ActionListener {
    //FileDialog dialog;
    @Override
    public void actionPerformed(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Smart Game Format (*.sgf)", "sgf")
        );
        int result = fileChooser.showSaveDialog(null);

        // 处理用户选择的结果
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            if(!selectedFile.getAbsolutePath().endsWith(".sgf"))
                filePath += ".sgf";
            System.out.println("保存文件: " + filePath);

            try {
                File manualFile = new File(filePath);
                if(!manualFile.exists()) {
                    manualFile.createNewFile();
                }
                BufferedWriter out = new BufferedWriter(
                        new FileWriter(manualFile)
                );
                writeManual(out);
                out.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        } else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("保存操作取消");
        }

//        dialog = new FileDialog(window, "save file", FileDialog.SAVE);
//        dialog.setFile("*.sgf");
//        dialog.setFilenameFilter(new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                return name.endsWith(".sgf");
//            }
//            @Override
//            public String getDescription() {
//                return "*.sgf files";
//            }
//        });
//        dialog.setVisible(true);
    }

    void writeManual(BufferedWriter out) throws IOException {
        out.write("(;SZ[" + SIZE + "]\n");
        if(game.mode == Mode.endGame) {
            char winner;
            if(game.res.winner.equals("black")) {
                winner = 'B';
            } else {
                winner = 'W';
            }
            if(game.res.diff == 0) {
                out.write(";RE[" + winner + "+R]\n");
            } else {
                out.write(";RE[" + winner + "+" + game.res.diff + "]\n");
            }
        }
        for(int i=1; i<=game.id; i++) {
            char color, x, y;
            if(game.moves.get(i).color == black) {
                color = 'B';
            } else {
                color = 'W';
            }
            x = (char)('a' + 18 - game.moves.get(i).x);
            y = (char)('a' + 18 - game.moves.get(i).y);
            out.write(";" + color + "[" + y + x +"]");
            if(i%10 == 0) {
                out.write("\n");
            }
        }
    }
}
