package global;

public class Config {

    public static final int
            SIZE = 19,
            RADIUS = 28/2,
            SQUARE = 12,
            CELL = 30,
            PADDING = 20,
            MARGIN = CELL - PADDING,
            SIDE = CELL,
            BOARD = CELL*(SIZE-1) + PADDING*2,
            PANEL = BOARD + MARGIN*2,
            SIDE_AREA = 300,
            MENU_HEIGHT = 60 + MARGIN*2,
            HEIGHT = PANEL + MENU_HEIGHT,
            WIDTH = PANEL + SIDE_AREA;
}
