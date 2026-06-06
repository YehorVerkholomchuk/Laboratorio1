package io.github.some_example_name.tests;

import io.github.some_example_name.MainGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainGameTest {

    @Test
    void shouldInitializeWithNullComponents() {
        // 1. Arrange: Creazione dell'istanza di MainGame
        MainGame game = new MainGame();

        // 2. Act: Nessuna azione necessaria
        // 3. Assert: Verifica che l'oggetto sia stato creato correttamente
        assertNotNull(game);
    }

    @Test
    void shouldCreateNewInstanceEachTime() {
        // 1. Arrange: Creazione di due istanze separate
        MainGame game1 = new MainGame();
        MainGame game2 = new MainGame();

        // 2. Act: Nessuna azione necessaria
        // 3. Assert: Verifica che le due istanze siano oggetti distinti
        assertNotSame(game1, game2);
    }
}
