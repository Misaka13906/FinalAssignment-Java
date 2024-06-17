//package finalassignment;

import component.*;
import global.GlobalVal;
import logic.GameManager;

public class Main {
    public static void main(String[] args) {
        GlobalVal.game = new GameManager();
        GlobalVal.window = new Window();
    }
}