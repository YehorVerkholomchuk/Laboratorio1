package io.github.some_example_name.tests;

import io.github.some_example_name.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {

    private Enemy enemy;

    @BeforeEach
    void setUp() {
        enemy = new Enemy();
    }

    @Test
    void shouldResetStateCorrectly() {
        // 1. Arrange: Configurazione del nemico con un bersaglio
        enemy.set(100f, 200f, true);

        // 2. Act: Reset del nemico
        enemy.reset();

        // 3. Assert: Verifica che il bersaglio sia stato azzerato
        assertEquals(0f, enemy.getTargetX());
        assertEquals(0f, enemy.getTargetY());
    }

    @Test
    void shouldApplyEasyDifficultyMultiplier() {
        // 1. Arrange: Nemico con difficoltà EASY
        // 2. Act: Applicazione della difficoltà
        enemy.applyDifficulty("EASY");

        // 3. Assert: Verifica che MAX_FORCE sia ridotta rispetto al valore base
        assertTrue(enemy.getMAX_FORCE() < 400f);
    }

    @Test
    void shouldApplyExtremeDifficultyMultiplier() {
        // 1. Arrange: Nemico con difficoltà EXTREME
        // 2. Act: Applicazione della difficoltà
        enemy.applyDifficulty("EXTREME");

        // 3. Assert: Verifica che MAX_FORCE sia aumentata rispetto al valore base
        assertTrue(enemy.getMAX_FORCE() > 400f);
    }

    @Test
    void shouldNotMoveBeforeLaunch() {
        // 1. Arrange: Nemico non ancora lanciato
        enemy.setX(50f);
        enemy.setY(50f);

        // 2. Act: Aggiornamento del nemico senza lancio
        enemy.move(0.016f);

        // 3. Assert: Verifica che la posizione non sia cambiata
        assertEquals(50f, enemy.getX());
        assertEquals(50f, enemy.getY());
    }
}
