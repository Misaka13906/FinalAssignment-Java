//package finalassignment;

import component.*;
import global.Global;
import logic.GameManager;

public class Main {
    public static void main(String[] args) {
        Global.game = new GameManager();
        Global.window = new Window();
    }
}