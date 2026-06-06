package io.github.some_example_name;

/**
 * Classe Tile
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class Tile {
    private final String type;
    private final float x, y, width, height;
    private final int gridX, gridY;

    /**
     * Costruisce una nuova tile della mappa.
     *
     * @param type il tipo della tile
     * @param x coordinata X
     * @param y coordinata Y
     * @param width larghezza della tile
     * @param height altezza della tile
     * @param gridX posizione X nella griglia
     * @param gridY posizione Y nella griglia
     */
    public Tile(String type, float x, float y, float width, float height, int gridX, int gridY) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public String getType() { return type; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
}
