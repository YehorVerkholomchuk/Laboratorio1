package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Classe MainGame
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class MainGame extends ApplicationAdapter {
    private Menu menu;
    private World world;
    private DrawRenderer draw;
    private AudioRenderer audio;

    /**
     * Inizializzazione del gioco e dei suoi elementi
     */
    @Override
    public void create() {
        draw = new DrawRenderer();
        audio = new AudioRenderer();

        menu = new Menu(audio);
        world = new World(menu, audio);

        try {
            world.create(Gdx.files.internal(menu.getMap()));
        } catch (Exception e) {
            Gdx.app.error("MainGame", "Failed to create world: " + e.getMessage());
        }

        audio.playMenuMusic();
    }

    /**
     * Render del gioco (ciclo di vita)
     */
    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        ControllerInput.ButtonState.update();
        audio.update(dt, menu);

        ScreenUtils.clear(0, 0, 0, 0);

        if (!menu.isGameOver() && menu.isRestart()) {
            menu.setRestart(false);
            restart();

        } else if (menu.isGameOver()) {
            menu.update(draw, audio);
            draw.drawMenu(menu, audio);

        } else {
            world.update(dt);
            draw.draw(world, menu);
            draw.drawAimLine(world.getPlayer());

            if (world.isGameOverAnim()) {
                draw.drawGameOver();
            }
        }
    }

    /**
     * Dispose degli elementi
     */
    @Override
    public void dispose() {
        draw.dispose();
        audio.dispose();
    }

    /**
     * Restart del gioco
     */
    private void restart() {
        audio.stopMusic();

        world = new World(menu, audio);

        try {
            world.create(Gdx.files.internal(menu.getMap()));
        } catch (Exception e) {
            Gdx.app.error("MainGame", "Failed to restart world: " + e.getMessage());
        }

        world.startIntro();
    }
}
