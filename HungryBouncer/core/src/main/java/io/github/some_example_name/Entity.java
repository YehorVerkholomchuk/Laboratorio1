package io.github.some_example_name;

/**
 * Classe astratta Entity
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public abstract class Entity implements Common {
    float x, y;
    float width = 40f, height = 40f;
    float speedX, speedY;
    float dragX, dragY;

    final float MIN_SPEED = 5f;
    final float FRICTION = 16f;
    final float MAX_DRAG = 600f;
    final float MAX_FORCE = 400f;

    /**
     * Gestisce il trascinamento dell'entità.
     */
    public abstract void drag();

    /**
     * Lancia l'entità applicando la forza accumulata.
     */
    public abstract void launch();

    /**
     * Muove l'entità nel tempo.
     *
     * @param dt il delta time del frame
     */
    public abstract void move(float dt);

    /**
     * Gestisce il rimbalzo dell'entità contro i bordi o ostacoli.
     */
    public abstract void bounce();

    /**
     * Ferma l'entità quando la velocità è minima.
     */
    public abstract void stop();

    /**
     * Aggiorna completamente lo stato dell'entità.
     *
     * @param dt il delta time del frame
     */
    public abstract void update(float dt);
}
