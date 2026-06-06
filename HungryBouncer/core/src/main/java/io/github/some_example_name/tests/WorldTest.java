package io.github.some_example_name.tests;

import io.github.some_example_name.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    private World world;

    @BeforeEach
    void setUp() {
        world = new World(null, null);
    }

    @Test
    void shouldNotBeIntroOnCreation() {
        // 1. Arrange: Mondo appena creato senza startIntro()
        // 2. Act: Verifica dello stato dell'intro
        boolean intro = world.isIntro();

        // 3. Assert: Verifica che l'intro non sia attiva
        assertFalse(intro);
    }

    @Test
    void shouldNotBeGameOverAnimOnCreation() {
        // 1. Arrange: Mondo appena creato
        // 2. Act: Verifica dello stato dell'animazione game over
        boolean anim = world.isGameOverAnim();

        // 3. Assert: Verifica che l'animazione non sia attiva
        assertFalse(anim);
    }

    @Test
    void shouldReturnZeroTimeOnCreation() {
        // 1. Arrange: Mondo appena creato (timer = 0)
        // 2. Act: Lettura del tempo di gioco
        int[] time = world.getTime();

        // 3. Assert: Verifica che il tempo sia zero
        assertArrayEquals(new int[]{0, 0, 0}, time);
    }

    @Test
    void shouldActivateIntroAfterStartIntro() {
        // 1. Arrange: Mondo appena creato
        // 2. Act: Avvio dell'intro
        world.startIntro();

        // 3. Assert: Verifica che l'intro sia attiva
        assertTrue(world.isIntro());
    }
}
