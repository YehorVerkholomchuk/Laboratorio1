package io.github.some_example_name.tests;

import io.github.some_example_name.Common;
import io.github.some_example_name.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(null);
    }

    @Test
    void shouldStartWithFullHp() {
        // 1. Arrange: Giocatore appena creato
        // 2. Act: Lettura degli HP correnti
        float hp = player.getHp();

        // 3. Assert: Verifica che gli HP iniziali siano al massimo
        assertEquals(Common.PLAYER_HP, hp);
    }

    @Test
    void shouldStartWithZeroFood() {
        // 1. Arrange: Giocatore appena creato
        // 2. Act: Lettura del cibo raccolto
        int food = player.getFood();

        // 3. Assert: Verifica che il cibo iniziale sia zero
        assertEquals(0, food);
    }

    @Test
    void shouldResetFoodToZero() {
        // 1. Arrange: Simulazione di cibo raccolto tramite setter
        player.setHp(50f);

        // 2. Act: Reset del cibo
        player.resetFood();

        // 3. Assert: Verifica che il cibo sia azzerato
        assertEquals(0, player.getFood());
    }

    @Test
    void shouldUpdateHpWithSetter() {
        // 1. Arrange: Giocatore con HP pieno
        // 2. Act: Impostazione degli HP a 50
        player.setHp(50f);

        // 3. Assert: Verifica che gli HP siano aggiornati
        assertEquals(50f, player.getHp());
    }

    @Test
    void shouldReturnCorrectCursorColorAtZeroPower() {
        // 1. Arrange: Giocatore senza trascinamento (dragLen = 0)
        // 2. Act: Lettura del colore del cursore
        float[] color = player.getCursorColor();

        // 3. Assert: Verifica che il colore sia rosso (potenza minima)
        assertEquals(1f, color[0]);
        assertEquals(0f, color[1]);
    }
}
