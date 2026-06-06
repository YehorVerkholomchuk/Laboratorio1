package io.github.some_example_name;

import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Classe World
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class World implements Common {
    private Player player;
    private Enemy enemy;
    private ArrayList<Tile> tiles;
    private int[][] map;

    private final Menu menu;
    private final AudioRenderer audio;
    private final SaveRenderer save = new SaveRenderer();
    private final Random random = new Random();

    private float enemyTimer, timer, introTimer, gameOverTimer;
    private boolean levelUp;
    private int foodCount, totalFood;

    /**
     * Costruisce il mondo di gioco.
     *
     * @param menu il menu principale
     * @param audio il renderer audio
     */
    public World(Menu menu, AudioRenderer audio) {
        this.menu = menu;
        this.audio = audio;
    }

    public Player getPlayer() { return player; }
    public Enemy getEnemy() { return enemy; }
    public int[][] getMap() { return map; }
    public float getIntroTimer() { return introTimer; }
    public int getFoodCount() { return foodCount; }

    /**
     * Verifica se l'introduzione è attiva.
     *
     * @return true se intro attiva
     */
    public boolean isIntro() { return introTimer > 0; }

    /**
     * Verifica se è attiva l'animazione di game over.
     *
     * @return true se animazione attiva
     */
    public boolean isGameOverAnim() { return gameOverTimer > 0; }

    /**
     * Restituisce il tempo di gioco formattato.
     *
     * @return array con minuti, secondi e centesimi
     */
    public int[] getTime() {
        return new int[]{(int)(timer/60), (int)(timer%60), (int)((timer*100)%100)};
    }

    /**
     * Crea il mondo e tutte le dipendenze (player, enemy..) caricando la mappa.
     *
     * @param mapFile file della mappa
     */
    public void create(FileHandle mapFile) {
        player = new Player(audio);
        enemy = new Enemy();
        enemy.applyDifficulty(menu.getDifficulty());
        enemyTimer = diffTimer();
        timer = 0; foodCount = FOOD_BASE; totalFood = 0; levelUp = true;

        try {
            map = loadMap(mapFile, ",");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load map: " + mapFile.path(), e);
        }

        tiles = new ArrayList<>();
        buildTiles();
        spawnFood();

        player.x = PLAYER_START_X; player.y = PLAYER_START_Y;
        enemy.x = ENEMY_START_X; enemy.y = ENEMY_START_Y;
    }

    /**
     * Avvia il timer introduttivo.
     */
    public void startIntro() { introTimer = INTRO_DURATION; }

    /**
     * Aggiorna tutto il mondo di gioco.
     *
     * @param dt delta time del frame
     */
    public void update(float dt) {
        if (introTimer > 0) { introTimer -= dt; return; }

        if (gameOverTimer > 0) {
            gameOverTimer -= dt;
            player.move(dt); player.bounce(); player.stop();
            enemy.move(dt); enemy.bounce(); enemy.stop();
            if (gameOverTimer <= 0) menu.setGameOver(true);
            return;
        }

        if (menu.isGameOver()) return;

        timer += dt;
        player.update(dt);
        player.hitTiles(tiles, map);
        tickEnemy(dt);
        enemy.update(dt);
        enemy.hitTiles(tiles, map);

        if (player.hit("enemy", enemy.x, enemy.y, enemy.width, enemy.height))
            player.tryTakeDamage(diffDamage());

        if (player.getHp() <= 0) {
            player.setHp(0);
            totalFood += player.getFood();
            save.trySave(totalFood, menu.getLevel(), menu.getDifficulty(), timer);
            gameOverTimer = GAME_OVER_DURATION;
            audio.playLose();
            audio.playGameOver();
        } else if (player.getFood() == foodCount && levelUp) {
            levelUp = false;
            totalFood += foodCount;
            foodCount += FOOD_INCREMENT;
            menu.setLevel(menu.getLevel() + 1);
            player.resetFood();
            spawnFood();
            levelUp = true;
            audio.playLevel();
        }
    }

    /**
     * Aggiorna il comportamento del nemico.
     *
     * @param dt delta time del frame
     */
    private void tickEnemy(float dt) {
        enemyTimer -= dt;
        if (enemyTimer <= ENEMY_THRESHOLD) {
            enemyTimer = diffTimer();
            enemy.reset();
            enemy.set(player.x, player.y, true);
        }
    }

    /**
     * Restituisce il timer nemico in base alla difficoltà.
     *
     * @return tempo tra gli attacchi del nemico
     */
    private float diffTimer() {
        switch (menu.getDifficulty()) {
            case "EASY": return ENEMY_TIMER_EASY;
            case "HARD": return ENEMY_TIMER_HARD;
            case "EXTREME": return ENEMY_TIMER_EXTREME;
            default: return ENEMY_TIMER_NORMAL;
        }
    }

    /**
     * Restituisce il danno del nemico in base alla difficoltà.
     *
     * @return quantità di danno
     */
    private float diffDamage() {
        switch (menu.getDifficulty()) {
            case "EASY": return ENEMY_DAMAGE_EASY;
            case "HARD": return ENEMY_DAMAGE_HARD;
            case "EXTREME": return ENEMY_DAMAGE_EXTREME;
            default: return ENEMY_DAMAGE_NORMAL;
        }
    }

    /**
     * Costruisce le tile muro della mappa.
     */
    private void buildTiles() {
        for (int y = 0; y < map.length; y++)
            for (int x = 0; x < map[y].length; x++)
                if (map[y][x] == TILE_WALL)
                    tiles.add(new Tile("wall", x*TILE_WIDTH, y*TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, x, y));
    }

    /**
     * Genera il cibo nelle celle libere.
     */
    private void spawnFood() {
        ArrayList<int[]> empty = new ArrayList<>();
        for (int y = 0; y < map.length; y++)
            for (int x = 0; x < map[y].length; x++)
                if (map[y][x] == 0) empty.add(new int[]{x, y});

        Collections.shuffle(empty, random);
        int count = Math.min(foodCount, empty.size());
        for (int i = 0; i < count; i++) {
            int x = empty.get(i)[0], y = empty.get(i)[1];
            map[y][x] = TILE_FOOD;
            tiles.add(new Tile("food", x*TILE_WIDTH, y*TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, x, y));
        }
    }

    /**
     * Carica la mappa da file.
     *
     * @param f file della mappa
     * @param delim separatore dei valori
     * @return matrice della mappa caricata
     */
    private int[][] loadMap(FileHandle f, String delim) {
        ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
        for (String line : f.readString().split("\\r?\\n")) {
            if (line.trim().isEmpty()) continue;
            ArrayList<Integer> row = new ArrayList<>();
            for (String v : line.split(delim)) {
                try { row.add(Integer.parseInt(v.trim())); }
                catch (NumberFormatException e) { row.add(0); }
            }
            rows.add(row);
        }
        if (rows.isEmpty()) throw new RuntimeException("Map is empty");
        int[][] result = new int[rows.size()][rows.get(0).size()];
        for (int y = 0; y < rows.size(); y++)
            for (int x = 0; x < rows.get(y).size(); x++)
                result[y][x] = rows.get(y).get(x);
        return result;
    }
}
