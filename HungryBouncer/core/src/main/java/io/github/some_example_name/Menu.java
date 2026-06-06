package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * Classe Menu
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class Menu implements Common {
    private final AudioRenderer audio;
    private final SaveRenderer save = new SaveRenderer();

    private String tab = "home";
    private Rectangle selectedRect;

    float playX = 590f, setupX = 570f;

    private int playerSkinIndex, enemySkinIndex, backgroundIndex;
    private int tileIndex, foodIndex, musicIndex, difficultyIndex, mapIndex;

    private final String[] difficulties = {"EASY", "NORMAL", "HARD", "EXTREME"};
    private final String[] maps = {"maps/hungryBouncer.txt", "maps/maze.txt"};
    private final String[] mapNames = {"Hungry Bouncer", "Maze"};

    private final Rectangle playRect, setupRect, recordsRect, quitRect;
    private final Rectangle playerSkinRect, enemySkinRect, backgroundRect;
    private final Rectangle tileRect, foodRect, musicRect, difficultyRect, mapRect, backRect;

    private boolean isGameOver = true, isRestart = true;
    private int level = 1;

    /**
     * Costruisce il menu principale del gioco.
     *
     * @param audio il renderer audio del gioco
     */
    public Menu(AudioRenderer audio) {
        this.audio = audio;
        playRect = r(BUTTON_X, PLAY_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        setupRect = r(BUTTON_X, SETUP_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        recordsRect = r(BUTTON_X, RECORDS_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        quitRect = r(BUTTON_X, QUIT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        playerSkinRect = r(SETUP_BUTTON_X,SETUP_PLAYER_Y, SETUP_BUTTON_WIDTH, SETUP_BUTTON_HEIGHT);
        enemySkinRect = r(SETUP_BUTTON_X,SETUP_ENEMY_Y, SETUP_BUTTON_WIDTH, SETUP_BUTTON_HEIGHT);
        backgroundRect = r(SETUP_BUTTON_X,SETUP_BG_Y, SETUP_BUTTON_WIDTH, SETUP_BUTTON_HEIGHT);
        tileRect = r(SETUP_COL2_X, SETUP_TILE_Y, SETUP_BUTTON_WIDTH, SETUP_BUTTON_HEIGHT);
        foodRect = r(SETUP_COL2_X, SETUP_FOOD_Y, SETUP_BUTTON_WIDTH, SETUP_BUTTON_HEIGHT);
        musicRect = r(SETUP_COL2_X, SETUP_MUSIC_Y, SETUP_BUTTON_WIDTH, SETUP_BUTTON_HEIGHT);
        difficultyRect = r(SETUP_COL3_X, SETUP_DIFF_Y, SETUP_BUTTON_WIDTH, SETUP_BUTTON_HEIGHT);
        mapRect = r(SETUP_COL3_X, SETUP_MAP_Y, SETUP_BUTTON_WIDTH, SETUP_BUTTON_HEIGHT);
        backRect = r(SETUP_BUTTON_X,SETUP_BACK_Y, SETUP_BUTTON_WIDTH, SETUP_BUTTON_HEIGHT);
    }

    /**
     * Crea un rettangolo.
     *
     * @param x coordinata X
     * @param y coordinata Y
     * @param w larghezza
     * @param h altezza
     * @return il rettangolo creato
     */
    private Rectangle r(float x, float y, float w, float h) { return new Rectangle(x, y, w, h); }

    public Rectangle getSelectedRect() { return selectedRect; }
    public String getTab() { return tab; }
    public float getPlayX() { return playX; }
    public float getSetupX() { return setupX; }
    public int getPlayerSkinIndex() { return playerSkinIndex; }
    public int getEnemySkinIndex() { return enemySkinIndex; }
    public int getBackgroundIndex() { return backgroundIndex; }
    public int getTileIndex() { return tileIndex; }
    public int getFoodIndex() { return foodIndex; }
    public int getMusicIndex() { return musicIndex; }
    public String getDifficulty() { return difficulties[difficultyIndex]; }
    public String getMap() { return maps[mapIndex]; }
    public String getMapName() { return mapNames[mapIndex]; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isRestart() { return isRestart; }
    public int getLevel() { return level; }
    public String[] getBestRun() { return save.loadStrings(); }
    public void setGameOver(boolean v) { isGameOver = v; }
    public void setRestart(boolean v) { isRestart  = v; }
    public void setLevel(int v) { level = v; }

    /**
     * Aggiorna il menu in base all’input.
     *
     * @param dr renderer grafico
     * @param ar renderer audio
     */
    public void update(DrawRenderer dr, AudioRenderer ar) {
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        switch (tab) {
            case "home": inputHome(mx, my); break;
            case "setup": inputSetup(mx, my, dr); break;
            case "records": inputRecords(mx, my); break;
        }
    }

    /**
     * Gestisce l’input nella schermata home.
     *
     * @param mx posizione mouse X
     * @param my posizione mouse Y
     */
    private void inputHome(float mx, float my) {
        selectedRect = hover(mx, my, playRect, setupRect, recordsRect, quitRect);
        if (ControllerInput.justPressed(BTN_START)) { play(); return; }
        if (!clicked()) return;
        if (is(playRect)) play();
        else if (is(setupRect)) { tab = "setup"; }
        else if (is(recordsRect)) { tab = "records"; }
        else if (is(quitRect)) quit();
        audio.playClick();
    }

    /**
     * Gestisce l’input nella schermata setup.
     *
     * @param mx posizione mouse X
     * @param my posizione mouse Y
     * @param dr renderer grafico
     */
    private void inputSetup(float mx, float my, DrawRenderer dr) {
        selectedRect = hover(mx, my,
            playerSkinRect, enemySkinRect, backgroundRect,
            tileRect, foodRect, musicRect, difficultyRect, mapRect, backRect);
        if (ControllerInput.justPressed(BTN_B)) {
            tab = "home"; selectedRect = null; audio.playClick(); return;
        }
        if (!clicked()) return;
        if (is(playerSkinRect)) playerSkinIndex = (playerSkinIndex + 1) % dr.getPlayerSkinCount();
        else if (is(enemySkinRect)) enemySkinIndex = (enemySkinIndex + 1) % dr.getEnemySkinCount();
        else if (is(backgroundRect)) backgroundIndex = (backgroundIndex  + 1) % dr.getBackgroundCount();
        else if (is(tileRect)) tileIndex = (tileIndex + 1) % dr.getTileCount();
        else if (is(foodRect)) foodIndex = (foodIndex + 1) % dr.getFoodCount();
        else if (is(musicRect)) musicIndex = (musicIndex + 1) % audio.getMusicCount();
        else if (is(difficultyRect)) difficultyIndex = (difficultyIndex + 1) % difficulties.length;
        else if (is(mapRect)) mapIndex = (mapIndex + 1) % maps.length;
        else if (is(backRect)) { tab = "home"; selectedRect = null; }
        audio.playClick();
    }

    /**
     * Gestisce l’input nella schermata record.
     *
     * @param mx posizione mouse X
     * @param my posizione mouse Y
     */
    private void inputRecords(float mx, float my) {
        selectedRect = hover(mx, my, backRect);
        if (clicked() && is(backRect)) { tab = "home"; selectedRect = null; audio.playClick(); }
    }

    /**
     * Restituisce il rettangolo attualmente evidenziato.
     *
     * @param mx posizione mouse X
     * @param my posizione mouse Y
     * @param rects lista rettangoli
     * @return rettangolo selezionato
     */
    private Rectangle hover(float mx, float my, Rectangle... rects) {
        for (Rectangle r : rects) if (r.contains(mx, my)) return r;
        return null;
    }

    /**
     * Verifica se è avvenuto un click valido.
     *
     * @return true se cliccato
     */
    private boolean clicked() {
        return selectedRect != null
            && (Gdx.input.isButtonJustPressed(0) || ControllerInput.justPressed(BTN_A));
    }

    /**
     * Controlla se un rettangolo è selezionato.
     *
     * @param r rettangolo da controllare
     * @return true se selezionato
     */
    private boolean is(Rectangle r) { return r.equals(selectedRect); }

    /**
     * Avvia una nuova partita.
     */
    public void play() {
        isGameOver = false; isRestart = true; level = 1;
        audio.stopMusic(); audio.playStart();
    }

    /**
     * Chiude il gioco.
     */
    public void quit() { audio.stopAll(); Gdx.app.exit(); }
}
