package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Classe DrawRenderer
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class DrawRenderer implements Disposable, Common {
    private final SpriteBatch    batch;
    private final ShapeRenderer  shape;
    private final BitmapFont     font, fontMenu, fontTitle, fontOption;

    private final Texture menuTex, hpTex;
    private final ArrayList<Texture> generics, playerSkins, enemySkins, backgrounds, tiles, foods;

    /**
     * Costruisce il renderer grafico e carica tutti elementi grafici.
     */
    public DrawRenderer() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        font = makeFont(GREEN, 2);
        fontOption = makeFont(Color.YELLOW, 3);
        fontMenu = makeFont(GREEN, 4);
        fontTitle = makeFont(Color.RED, 6);

        generics = new ArrayList<>();
        backgrounds = new ArrayList<>();

        menuTex = tex("images/menu.png");
        hpTex = tex("images/hp.png");

        String[] gPaths = {
            "images/fabio.png","images/creeper.png","images/pizza.png","images/steve.png",
            "images/bouncer.png",
            "images/potion.png","images/cookie.png","images/gapple.png","images/falafel.png",
            "images/wall.png","images/blue.png","images/brick.png","images/cell.png",
            "images/diamond.png","images/emerald.png","images/eye.png","images/grass.png",
            "images/lucky.png","images/magma.png","images/poop1.png","images/poop2.png",
            "images/red.png","images/sand.png","images/tnt.png"
        };
        for (String p : gPaths) generics.add(tex(p));

        playerSkins = cloneList(generics);
        playerSkins.set(0, tex("images/bouncer.png"));
        playerSkins.set(4, tex("images/fabio.png"));

        enemySkins = cloneList(generics);
        enemySkins.set(0, tex("images/creeper.png"));
        enemySkins.set(1, tex("images/fabio.png"));

        foods = cloneList(generics);
        foods.set(0,  tex("images/falafel.png"));
        foods.set(11, tex("images/fabio.png"));

        tiles = cloneList(generics);
        tiles.set(0,  tex("images/wall.png"));
        tiles.set(12, tex("images/fabio.png"));

        String[] bgPaths = {
            "images/bg/windows_xp.jpg","images/bg/minecraft_dirt.jpg","images/bg/minecraft_old.jpg",
            "images/bg/minecraft_nether.jpg","images/bg/space.jpg","images/bg/mario.jpg"
        };
        for (String p : bgPaths) backgrounds.add(tex(p));

        Gdx.gl.glLineWidth(5);
    }

    /**
     * Ritorna una texture dal path
     *
     * @param path la texture da disegnare
     */
    private Texture tex(String path) {
        try { return new Texture(path); }
        catch (Exception e) { Gdx.app.error("Draw", "Missing texture: " + path); return null; }
    }

    /**
     * Genera un font di tipo bitmap
     *
     * @param color colore del font
     * @param scale dimensione del font
     */
    private BitmapFont makeFont(Color color, float scale) {
        BitmapFont f = new BitmapFont();
        f.setColor(color);
        f.getData().setScale(scale);
        return f;
    }

    /**
     * Ritorna una copia della lista delle texture
     *
     * @param src lista delle texture
     */
    private ArrayList<Texture> cloneList(ArrayList<Texture> src) { return (ArrayList<Texture>) src.clone(); }

    public int getPlayerSkinCount() { return playerSkins.size(); }
    public int getEnemySkinCount() { return enemySkins.size(); }
    public int getBackgroundCount() { return backgrounds.size(); }
    public int getTileCount() { return tiles.size(); }
    public int getFoodCount() { return foods.size(); }
    public Texture getPlayerSkin(int i) { return playerSkins.get(i); }
    public Texture getEnemySkin(int i) { return enemySkins.get(i); }
    public Texture getBackground(int i) { return backgrounds.get(i); }
    public Texture getTile(int i) { return tiles.get(i); }
    public Texture getFood(int i) { return foods.get(i); }

    /**
     * Disegna il gioco e tutti gli elementi dell'UI.
     *
     * @param world il mondo del gioco
     * @param menu menu del gioco
     */
    public void draw(World world, Menu menu) {
        batch.begin();
        batch.draw(getBackground(menu.getBackgroundIndex()), 0, 0, MAP_WIDTH, MAP_HEIGHT);
        drawMap(world.getMap(), menu.getTileIndex(), menu.getFoodIndex());

        Player p = world.getPlayer();
        Enemy  e = world.getEnemy();
        batch.draw(getPlayerSkin(menu.getPlayerSkinIndex()), p.x, p.y, p.width, p.height);
        batch.draw(getEnemySkin(menu.getEnemySkinIndex()),   e.x, e.y, e.width, e.height);

        batch.draw(hpTex, 10, HEIGHT-37, 30, 30);
        batch.draw(getFood(menu.getFoodIndex()), 210, HEIGHT-37, 30, 30);

        font.draw(batch, String.format("%.0f", p.getHp()), 50, HEIGHT-10);
        font.draw(batch, p.getFood() + " / " + world.getFoodCount(), 250, HEIGHT-10);
        font.draw(batch, "LVL: " + menu.getLevel(), 440, HEIGHT-10);
        font.draw(batch, menu.getDifficulty(), 640, HEIGHT-10);
        font.draw(batch, String.format("%02d:%02d:%02d", world.getTime()[0],
                                       world.getTime()[1], world.getTime()[2]), 900, HEIGHT-10);
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 1150, HEIGHT-10);

        if (p.getReloadTimer() > 0)
            font.draw(batch, String.format("%.1f", Math.abs(p.getReloadTimer())), p.x, p.y-10);
        if (world.isIntro())
            fontTitle.draw(batch, String.format("%.0f", world.getIntroTimer()), 600, 500);

        batch.end();
    }

    /**
     * Disegna il cursore quando il giocatore mira
     *
     * @param player giocatore
     */
    public void drawAimLine(Player player) {
        if (!player.isHolding() || !Gdx.input.isButtonPressed(0)) return;
        float[] c = player.getCursorColor();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(c[0], c[1], c[2], 1f);
        shape.line(player.getStartX(), player.getStartY(), player.getEndX(), player.getEndY());
        shape.end();
    }

    /**
     * Disegna il menu e le schede
     *
     * @param menu menu del gioco
     * @param audio audio renderer
     */
    public void drawMenu(Menu menu, AudioRenderer audio) {
        switch (menu.getTab()) {
            case "home": drawHome(menu); break;
            case "setup": drawSetup(menu, audio); break;
            case "records": drawRecords(menu); break;
        }
    }

    /**
     * Disegna la scheda Home del menu
     *
     * @param menu menu del gioco
     */
    public void drawHome(Menu menu) {
        batch.begin();
        batch.draw(menuTex, 0, 0, WIDTH, HEIGHT);
        fontMenu.draw(batch, "Play", menu.getPlayX(), 530);
        fontMenu.draw(batch, "Setup", menu.getSetupX(), 440);
        fontMenu.draw(batch, "Records", 538, 350);
        fontMenu.draw(batch, "Quit", 590, 170);
        batch.end();
        drawHighlight(menu.getSelectedRect());
    }

    /**
     * Disegna la scheda Setup del menu
     *
     * @param menu menu del gioco
     * @param audio audio renderer
     */
    public void drawSetup(Menu menu, AudioRenderer audio) {
        batch.begin();
        drawSafe(getPlayerSkin(menu.getPlayerSkinIndex()), 180, SETUP_PLAYER_Y-140, 120, 120);
        drawSafe(getEnemySkin(menu.getEnemySkinIndex()), 180, SETUP_ENEMY_Y-140, 120, 120);
        drawSafe(getBackground(menu.getBackgroundIndex()), 180, SETUP_BG_Y-140, 180, 90);
        drawSafe(getTile(menu.getTileIndex()), 600, SETUP_TILE_Y-120, 80, 80);
        drawSafe(getFood(menu.getFoodIndex()), 600, SETUP_FOOD_Y-120, 80, 80);

        fontMenu.draw(batch, "Player Skin", SETUP_BUTTON_X+15, SETUP_PLAYER_Y+60);
        fontMenu.draw(batch, "Enemy Skin", SETUP_BUTTON_X+8, SETUP_ENEMY_Y+60);
        fontMenu.draw(batch, "Background", SETUP_BUTTON_X+8, SETUP_BG_Y+60);
        fontMenu.draw(batch, "Tiles", SETUP_COL2_X+100, SETUP_TILE_Y+60);
        fontMenu.draw(batch, "Food", SETUP_COL2_X+90, SETUP_FOOD_Y+60);
        fontMenu.draw(batch, "Music", SETUP_COL2_X+90, SETUP_MUSIC_Y+60);
        fontMenu.draw(batch, "Difficulty", SETUP_COL3_X, SETUP_DIFF_Y+60);
        fontMenu.draw(batch, "Map", SETUP_COL3_X, SETUP_MAP_Y+60);
        fontMenu.draw(batch, "Home", SETUP_BUTTON_X+80, SETUP_BACK_Y+60);

        fontOption.draw(batch, audio.getMusicName(menu.getMusicIndex()), SETUP_COL2_X+85, SETUP_MUSIC_Y-20);
        fontOption.draw(batch, menu.getDifficulty(), SETUP_COL3_X, SETUP_DIFF_Y-20);
        fontOption.draw(batch, menu.getMapName(), SETUP_COL3_X, SETUP_MAP_Y-20);
        batch.end();
        drawHighlight(menu.getSelectedRect());
    }

    /**
     * Disegna la scheda Records del menu
     *
     * @param menu menu del gioco
     */
    public void drawRecords(Menu menu) {
        String[] b = menu.getBestRun();
        int secs = 0;
        try { secs = Integer.parseInt(b[3]); } catch (NumberFormatException ignored) {}

        batch.begin();
        fontMenu.draw(batch, "Best Run", 490, 820);
        fontOption.draw(batch, "Food:       " + b[0], 440, 650);
        fontOption.draw(batch, "Level:      " + b[1], 440, 560);
        fontOption.draw(batch, "Difficulty: " + b[2], 440, 470);
        fontOption.draw(batch, "Time:       " + fmt(secs), 440, 380);
        fontOption.draw(batch, "Date:       " + b[4], 440, 290);
        fontMenu.draw(batch, "Home", SETUP_BUTTON_X+80, SETUP_BACK_Y+60);
        batch.end();
        drawHighlight(menu.getSelectedRect());
    }

    /**
     * Disegna il messaggio Game Over
     */
    public void drawGameOver() {
        batch.begin();
        fontTitle.draw(batch, "GAME OVER", 360, 500);
        batch.end();
    }

    /**
     * Disegna la mappa del gioco
     *
     * @param map mappa del gioco
     * @param tileIdx indice della posizione corrente della tile
     * @param foodIdx indice della posizione corrente del cibo
     */
    private void drawMap(int[][] map, int tileIdx, int foodIdx) {
        for (int y = 0; y < map.length; y++)
            for (int x = 0; x < map[y].length; x++) {
                Texture t = null;
                if (map[y][x] == TILE_WALL) t = getTile(tileIdx);
                else if (map[y][x] == TILE_FOOD) t = getFood(foodIdx);
                if (t != null) batch.draw(t, x*TILE_WIDTH, y*TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }
    }

    /**
     * Disegna l'effetto hover su un bottone
     *
     * @param r il rettangolo (bottone)
     */
    private void drawHighlight(Rectangle r) {
        if (r == null) return;
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(GREEN);
        shape.rect(r.x, r.y, r.width, r.height);
        shape.end();
    }

    /**
     * Disegna una texture se non è null
     *
     * @param t texture
     * @param x posizione x
     * @param y posizione y
     * @param w larghezza
     * @param h altezza
     */
    private void drawSafe(Texture t, float x, float y, float w, float h) {
        if (t != null) batch.draw(t, x, y, w, h);
    }

    /**
     * Ritorna il tempo nel formato mm:ss
     *
     * @param s secondi
     * @return tempo nel formato mm:ss
     */
    private String fmt(int s) { return String.format("%02d:%02d", s/60, s%60); }

    /**
     * Dispose degli elementi
     */
    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        font.dispose(); fontMenu.dispose(); fontTitle.dispose(); fontOption.dispose();
        menuTex.dispose(); hpTex.dispose();
        for (Texture t : playerSkins) { if (t != null) t.dispose(); }
        for (Texture t : enemySkins) { if (t != null) t.dispose(); }
        for (Texture t : backgrounds) { if (t != null) t.dispose(); }
        for (Texture t : tiles) { if (t != null) t.dispose(); }
        for (Texture t : foods) { if (t != null) t.dispose(); }
    }
}
