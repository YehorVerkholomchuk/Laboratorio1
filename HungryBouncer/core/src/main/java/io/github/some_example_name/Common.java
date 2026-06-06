package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;

/**
 * Interfaccia Common
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public interface Common {
    Color GREEN = new Color(35f / 255f, 255f, 4f / 255f, 1f);

    float PLAYER_START_X = 610f;
    float PLAYER_START_Y = 440f;
    float PLAYER_HP = 100f;
    float PLAYER_INVINCIBLE_DURATION = 0.5f;
    float PLAYER_RELOAD_DURATION = 2f;

    float ENEMY_START_X = 50f;
    float ENEMY_START_Y = 50f;
    float ENEMY_THRESHOLD = 0.1f;
    float ENEMY_TIMER_EASY = 3.5f;
    float ENEMY_TIMER_NORMAL = 2.0f;
    float ENEMY_TIMER_HARD = 1.2f;
    float ENEMY_TIMER_EXTREME = 0.6f;
    float ENEMY_DAMAGE_EASY = 15f;
    float ENEMY_DAMAGE_NORMAL = 30f;
    float ENEMY_DAMAGE_HARD = 45f;
    float ENEMY_DAMAGE_EXTREME = 70f;

    float EASY_MULT = 0.6f;
    float NORMAL_MULT = 1.0f;
    float HARD_MULT = 1.5f;
    float EXTREME_MULT = 2.2f;

    int WIDTH = 1280;
    int HEIGHT = 960;
    int MAP_WIDTH = 1280;
    int MAP_HEIGHT = 920;
    float TILE_WIDTH = 20f;
    float TILE_HEIGHT = 20f;
    int TILE_WALL = 1;
    int TILE_FOOD = 2;

    float FOOD_HEAL = 10f;
    int FOOD_BASE = 10;
    int FOOD_INCREMENT = 5;

    float BUTTON_X = 525f;
    float BUTTON_WIDTH = 250;
    float BUTTON_HEIGHT = 70;
    float PLAY_Y = 470f;
    float SETUP_Y = 380f;
    float QUIT_Y = 110;
    float INTRO_DURATION = 3.5f;
    float GAME_OVER_DURATION = 2f;

    float SETUP_BUTTON_X = 80f;
    float SETUP_BUTTON_WIDTH = 320f;
    float SETUP_BUTTON_HEIGHT = 70f;
    float SETUP_COL2_X = 480f;
    float SETUP_COL3_X = 900f;

    float SETUP_PLAYER_Y = 840f;
    float SETUP_ENEMY_Y = 600f;
    float SETUP_BG_Y = 360f;
    float SETUP_TILE_Y = 840f;
    float SETUP_FOOD_Y = 600f;
    float SETUP_MUSIC_Y = 360f;
    float SETUP_DIFF_Y = 840f;
    float SETUP_MAP_Y = 600f;
    float SETUP_BACK_Y = 60f;

    float RECORDS_Y = 290f;

    int AXIS_LEFT_X = 1;
    int AXIS_LEFT_Y = 0;

    int BTN_A = 0;
    int BTN_B = 1;
    int BTN_START = 9;

    float STICK_DEADZONE = 0.15f;
}
