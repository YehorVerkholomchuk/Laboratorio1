package io.github.some_example_name;

import java.util.ArrayList;

/**
 * Classe Enemy
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class Enemy extends Entity implements Common {
    private boolean hasLaunched, isLaunching;
    private float targetX, targetY;

    private float MAX_FORCE = 400f;
    private float FRICTION = 16f;

    public float getTargetX() {
        return targetX;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public float getMAX_FORCE() {
        return MAX_FORCE;
    }

    public void setMAX_FORCE(float MAX_FORCE) {
        this.MAX_FORCE = MAX_FORCE;
    }

    public float getFRICTION() {
        return FRICTION;
    }

    public void setFRICTION(float FRICTION) {
        this.FRICTION = FRICTION;
    }

    /**
     * Costruisce un nuovo nemico.
     */
    public Enemy() {
        super();
    }

    /**
     * Applica i parametri di difficoltà al nemico.
     *
     * @param diff il livello di difficoltà selezionato
     */
    public void applyDifficulty(String diff) {
        float m;

        switch (diff) {
            case "EASY": m = EASY_MULT; break;
            case "HARD": m = HARD_MULT; break;
            case "EXTREME": m = EXTREME_MULT; break;
            default: m = NORMAL_MULT;
        }

        MAX_FORCE = 400f * m;
        FRICTION = 16f / m;
    }

    /**
     * Imposta il bersaglio e lo stato di lancio del nemico.
     *
     * @param tx coordinata X del bersaglio
     * @param ty coordinata Y del bersaglio
     * @param launching true se il nemico deve essere lanciato
     */
    public void set(float tx, float ty, boolean launching) {
        targetX = tx;
        targetY = ty;
        isLaunching = launching;
    }

    /**
     * Ripristina lo stato iniziale del nemico.
     */
    public void reset() {
        isLaunching = false;
        hasLaunched = false;
        targetX = 0;
        targetY = 0;
    }

    /**
     * Verifica se il nemico collide con un oggetto.
     *
     * @param ox coordinata X dell'oggetto
     * @param oy coordinata Y dell'oggetto
     * @param ow larghezza dell'oggetto
     * @param oh altezza dell'oggetto
     * @return true se c'è collisione
     */
    private boolean overlaps(float ox, float oy, float ow, float oh) {
        return x < ox + ow && x + width > ox && y < oy + oh && y + height > oy;
    }

    /**
     * Gestisce la collisione con un oggetto.
     *
     * @param type tipo dell'oggetto
     * @param ox coordinata X dell'oggetto
     * @param oy coordinata Y dell'oggetto
     * @param ow larghezza dell'oggetto
     * @param oh altezza dell'oggetto
     * @return true se la collisione è avvenuta
     */
    public boolean hit(String type, float ox, float oy, float ow, float oh) {
        if (!overlaps(ox, oy, ow, oh)) return false;

        if (type.equals("food")) return true;

        float left = Math.abs((x + width) - ox);
        float right = Math.abs(x - (ox + ow));
        float bottom = Math.abs((y + height) - oy);
        float top = Math.abs(y - (oy + oh));

        float min = Math.min(Math.min(left, right), Math.min(top, bottom));

        if (min == left) {
            x = ox - width;
            speedX *= -1;
        } else if (min == right) {
            x = ox + ow;
            speedX *= -1;
        } else if (min == bottom) {
            y = oy - height;
            speedY *= -1;
        } else {
            y = oy + oh;
            speedY *= -1;
        }

        return true;
    }

    /**
     * Gestisce le collisioni con le tile della mappa.
     *
     * @param tiles lista delle tile
     * @param map matrice della mappa
     */
    public void hitTiles(ArrayList<Tile> tiles, int[][] map) {
        for (int i = tiles.size() - 1; i >= 0; i--) {
            Tile t = tiles.get(i);

            if (t.getType().equals("wall")) {
                hit("wall", t.getX(), t.getY(), t.getWidth(), t.getHeight());
            }
        }
    }

    /**
     * Calcola il vettore di trascinamento verso il bersaglio.
     */
    @Override
    public void drag() {
        dragX = targetX - x;
        dragY = targetY - y;

        float len = (float) Math.sqrt(dragX * dragX + dragY * dragY);

        if (len > MAX_DRAG) {
            float s = MAX_DRAG / len;
            dragX *= s;
            dragY *= s;
        }
    }

    /**
     * Lancia il nemico verso il bersaglio.
     */
    @Override
    public void launch() {
        if (!isLaunching || hasLaunched) return;

        hasLaunched = true;

        speedX = (dragX / MAX_DRAG) * MAX_FORCE;
        speedY = (dragY / MAX_DRAG) * MAX_FORCE;
    }

    /**
     * Muove il nemico nel tempo.
     *
     * @param dt il delta time del frame
     */
    @Override
    public void move(float dt) {
        if (!hasLaunched) return;

        if (speedX > 0) speedX -= FRICTION * dt;
        else if (speedX < 0) speedX += FRICTION * dt;

        if (speedY > 0) speedY -= FRICTION * dt;
        else if (speedY < 0) speedY += FRICTION * dt;

        x += speedX * dt;
        y += speedY * dt;
    }

    /**
     * Gestisce il rimbalzo del nemico ai bordi della mappa.
     */
    @Override
    public void bounce() {
        if (!hasLaunched) return;

        if (x < 0) { x = 0; speedX *= -1; }
        if (x > MAP_WIDTH - width) { x = MAP_WIDTH - width; speedX *= -1; }

        if (y < 0) { y = 0; speedY *= -1; }
        if (y > MAP_HEIGHT - height) { y = MAP_HEIGHT - height; speedY *= -1; }
    }

    /**
     * Arresta il nemico quando la velocità è minima.
     */
    @Override
    public void stop() {
        if (Math.abs(speedX) < MIN_SPEED) speedX = 0;
        if (Math.abs(speedY) < MIN_SPEED) speedY = 0;

        if (speedX == 0 && speedY == 0) hasLaunched = false;
    }

    /**
     * Aggiorna completamente lo stato del nemico.
     *
     * @param dt il delta time del frame
     */
    @Override
    public void update(float dt) {
        drag();
        launch();
        move(dt);
        bounce();
        stop();
    }
}
