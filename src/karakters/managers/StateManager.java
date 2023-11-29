package karakters.managers;

import karakters.enums.GAME_STATE;

// game settings????
public class StateManager {
    public static int[][] GAME_RESOLUTIONS = new int[][]{
            {720, 480},
            {1280, 720},
            {1920, 1080}
    };

    public static int
    GAME_WIDTH = 720,
    GAME_HEIGHT = 480,
    GAME_RESOLUTION = 0;

    // STATE MACHINE!!!!!!!
    public static boolean
    MUSIC = false,
    SFX = false,
    FULLSCREEN = false,
    DEV_HITBOX_ESP_LINES = false,
    DEV_FREEZE_BOTS = false,
    DEV_FREEZE_WORLD = false,
    DEV_GOD_MODE = false;

    public GAME_STATE gameState;
}
