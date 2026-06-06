package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/**
 * Classe Player
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class Player extends Entity implements Common {
    private final AudioRenderer audio;
    private float hp = PLAYER_HP;
    private int food;
    private boolean isHolding, hasLaunched;
    private float invincibleTimer, reloadTimer, dragLen;
    private float startX, startY, endX, endY;

    /**
     * Costruisce il giocatore inizializzando il sistema audio.
     *
     * @param audio il renderer audio del gioco
     */
    public Player(AudioRenderer audio) { super(); this.audio = audio; }

    public float getHp() { return hp; }
    public int getFood() { return food; }
    public float getReloadTimer() { return reloadTimer; }
    public boolean isHolding() { return isHolding; }
    public float getStartX() { return startX; }
    public float getStartY() { return startY; }
    public float getEndX() { return endX; }
    public float getEndY() { return endY; }
    public void setHp(float hp) { this.hp = hp; }
    /**
     * Azzera il contatore del cibo raccolto.
     */
    public void resetFood() { food = 0; }

    /**
     * Controlla la sovrapposizione con un rettangolo.
     *
     * @param ox coordinata X oggetto
     * @param oy coordinata Y oggetto
     * @param ow larghezza oggetto
     * @param oh altezza oggetto
     * @return true se c'è collisione
     */
    private boolean overlaps(float ox, float oy, float ow, float oh) {
        return x < ox+ow && x+width > ox && y < oy+oh && y+height > oy;
    }

    /**
     * Applica danno al giocatore se possibile.
     *
     * @param amount quantità di danno
     * @return true se il danno è stato applicato
     */
    public boolean tryTakeDamage(float amount) {
        if (invincibleTimer > 0) return false;
        hp = Math.max(hp - amount, 0);
        invincibleTimer = PLAYER_INVINCIBLE_DURATION;
        audio.playHit();
        return true;
    }

    /**
     * Gestisce la collisione con un oggetto.
     *
     * @param type tipo oggetto
     * @param ox coordinata X oggetto
     * @param oy coordinata Y oggetto
     * @param ow larghezza oggetto
     * @param oh altezza oggetto
     * @return true se è avvenuta collisione
     */
    public boolean hit(String type, float ox, float oy, float ow, float oh) {
        if (!overlaps(ox, oy, ow, oh)) return false;
        if (type.equals("food")) return true;

        float left = Math.abs((x+width)-ox), right = Math.abs(x-(ox+ow));
        float bottom = Math.abs((y+height)-oy), top = Math.abs(y-(oy+oh));
        float min = Math.min(Math.min(left, right), Math.min(top, bottom));

        if      (min == left)   { x = ox-width;  speedX *= -1; }
        else if (min == right)  { x = ox+ow;     speedX *= -1; }
        else if (min == bottom) { y = oy-height; speedY *= -1; }
        else                    { y = oy+oh;     speedY *= -1; }

        if (type.equals("wall")) audio.playBounce();
        return true;
    }

    /**
     * Gestisce collisioni con tutte le tile.
     *
     * @param tiles lista delle tile
     * @param map mappa di gioco
     */
    public void hitTiles(ArrayList<Tile> tiles, int[][] map) {
        for (int i = tiles.size()-1; i >= 0; i--) {
            Tile t = tiles.get(i);
            if (t.getType().equals("wall")) {
                hit("wall", t.getX(), t.getY(), t.getWidth(), t.getHeight());
            } else if (t.getType().equals("food")) {
                if (hit("food", t.getX(), t.getY(), t.getWidth(), t.getHeight())) {
                    hp = Math.min(hp + FOOD_HEAL, PLAYER_HP);
                    food++;
                    map[t.getGridY()][t.getGridX()] = 0;
                    tiles.remove(i);
                    audio.playEat();
                }
            }
        }
    }

    /**
     * Restituisce il colore del cursore in base alla potenza.
     *
     * @return array RGB del colore
     */
    public float[] getCursorColor() {
        float power = Math.min(dragLen/MAX_DRAG, 1f);
        float r, g, b = 0.2f;
        if (power < 0.5f) { r = 1f; g = power/0.5f; }
        else { r = 1f - (power-0.5f)/0.5f; g = 1f; }
        return new float[]{r, g, b};
    }

    /**
     * Gestisce l'input del giocatore.
     */
    private void input() {
        if (reloadTimer > 0) return;
        if (Gdx.input.isButtonJustPressed(0) || ControllerInput.justPressed(BTN_A)) {
            isHolding = true; hasLaunched = false;
            speedX = 0; speedY = 0;
            reloadTimer = PLAYER_RELOAD_DURATION;
        }
    }

    /**
     * Gestisce il trascinamento per la mira.
     */
    @Override
    public void drag() {
        if (!isHolding) return;
        if (Gdx.input.isButtonPressed(0)) {
            float mx = Gdx.input.getX();
            float my = MAP_HEIGHT - Gdx.input.getY();
            dragX = x - mx; dragY = y - my;
            dragLen = (float) Math.sqrt(dragX*dragX + dragY*dragY);
            if (dragLen > MAX_DRAG) { float s = MAX_DRAG/dragLen; dragX *= s; dragY *= s; }
        } else if (ControllerInput.isConnected()) {
            float sx = ControllerInput.axis(AXIS_LEFT_X);
            float sy = ControllerInput.axis(AXIS_LEFT_Y);
            if (sx != 0 || sy != 0) {
                dragX = -sx * MAX_DRAG; dragY = sy * MAX_DRAG;
                dragLen = (float) Math.sqrt(dragX*dragX + dragY*dragY);
            }
        }
        startX = x + width/2f; startY = y + height/2f;
        endX = startX + dragX; endY = startY + dragY;
    }

    /**
     * Lancia il giocatore.
     */
    @Override
    public void launch() {
        if (!isHolding) return;
        boolean mouseUp = !Gdx.input.isButtonPressed(0);
        boolean ctrlUp  = ControllerInput.isConnected() && !ControllerInput.pressed(BTN_A);
        if (!mouseUp && !ctrlUp) return;
        isHolding = false; hasLaunched = true;
        speedX = (dragX/MAX_DRAG) * MAX_FORCE;
        speedY = (dragY/MAX_DRAG) * MAX_FORCE;
        audio.playLaunch();
    }

    /**
     * Muove il giocatore nel tempo.
     *
     * @param dt delta time del frame
     */
    @Override
    public void move(float dt) {
        if (!hasLaunched) return;
        if (speedX > 0) speedX -= FRICTION*dt; else if (speedX < 0) speedX += FRICTION*dt;
        if (speedY > 0) speedY -= FRICTION*dt; else if (speedY < 0) speedY += FRICTION*dt;
        x += speedX*dt; y += speedY*dt;
        if (reloadTimer > 0) reloadTimer -= dt;
    }

    /**
     * Gestisce il rimbalzo ai bordi.
     */
    @Override
    public void bounce() {
        if (!hasLaunched) return;
        if (x < 0)                 { x = 0;              speedX *= -1; audio.playBounce(); }
        if (x > MAP_WIDTH-width)   { x = MAP_WIDTH-width;   speedX *= -1; audio.playBounce(); }
        if (y < 0)                 { y = 0;              speedY *= -1; audio.playBounce(); }
        if (y > MAP_HEIGHT-height) { y = MAP_HEIGHT-height; speedY *= -1; audio.playBounce(); }
    }

    /**
     * Arresta il movimento se la velocità è minima.
     */
    @Override
    public void stop() {
        if (Math.abs(speedX) < MIN_SPEED) speedX = 0;
        if (Math.abs(speedY) < MIN_SPEED) speedY = 0;
        if (speedX == 0 && speedY == 0) hasLaunched = false;
    }

    /**
     * Aggiorna completamente il giocatore.
     *
     * @param dt delta time del frame
     */
    @Override
    public void update(float dt) {
        if (invincibleTimer > 0) invincibleTimer -= dt;
        input(); drag(); launch(); move(dt); bounce(); stop();
    }
}
