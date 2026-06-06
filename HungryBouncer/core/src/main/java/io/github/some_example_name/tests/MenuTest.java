package io.github.some_example_name.tests;

import io.github.some_example_name.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu(null);
    }

    @Test
    void shouldStartOnHomeTab() {
        // 1. Arrange: Menu appena creato
        // 2. Act: Lettura della scheda corrente
        String tab = menu.getTab();

        // 3. Assert: Verifica che la scheda iniziale sia home
        assertEquals("home", tab);
    }

    @Test
    void shouldBeGameOverOnCreation() {
        // 1. Arrange: Menu appena creato
        // 2. Act: Lettura dello stato di game over
        boolean gameOver = menu.isGameOver();

        // 3. Assert: Verifica che lo stato iniziale sia game over
        assertTrue(gameOver);
    }

    @Test
    void shouldSetGameOverCorrectly() {
        // 1. Arrange: Menu con game over attivo
        // 2. Act: Impostazione del game over a false
        menu.setGameOver(false);

        // 3. Assert: Verifica che il valore sia aggiornato
        assertFalse(menu.isGameOver());
    }

    @Test
    void shouldSetLevelCorrectly() {
        // 1. Arrange: Menu con livello iniziale a 1
        // 2. Act: Impostazione del livello a 5
        menu.setLevel(5);

        // 3. Assert: Verifica che il livello sia aggiornato
        assertEquals(5, menu.getLevel());
    }

    @Test
    void shouldReturnDefaultDifficulty() {
        // 1. Arrange: Menu appena creato (indice difficoltà = 0)
        // 2. Act: Lettura della difficoltà corrente
        String difficulty = menu.getDifficulty();

        // 3. Assert: Verifica che la difficoltà di default sia EASY
        assertEquals("EASY", difficulty);
    }

    @Test
    void shouldReturnDefaultMap() {
        // 1. Arrange: Menu appena creato (indice mappa = 0)
        // 2. Act: Lettura della mappa corrente
        String map = menu.getMap();

        // 3. Assert: Verifica che la mappa di default sia corretta
        assertEquals("maps/hungryBouncer.txt", map);
    }
}
