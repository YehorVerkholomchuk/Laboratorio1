package io.github.some_example_name.tests;

import io.github.some_example_name.ControllerInput;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControllerInputTest {

    @Test
    void shouldReturnFalseWhenButtonNotPressed() {
        // 1. Arrange: Nessun pulsante premuto (stato iniziale)
        // 2. Act: Verifica se il pulsante 0 è stato appena premuto
        boolean result = ControllerInput.ButtonState.justPressed(0);

        // 3. Assert: Verifica che il pulsante non risulti premuto
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForOutOfBoundsButton() {
        // 1. Arrange: Indice del pulsante fuori dai limiti dell'array
        int invalidButton = 999;

        // 2. Act: Verifica il pulsante con indice non valido
        boolean result = ControllerInput.ButtonState.justPressed(invalidButton);

        // 3. Assert: Verifica che ritorni false senza eccezioni
        assertFalse(result);
    }

    @Test
    void shouldUpdateButtonStateWithoutCrashing() {
        // 1. Arrange: Nessun controller connesso (ambiente di test)
        // 2. Act: Aggiornamento dello stato dei pulsanti
        // 3. Assert: Verifica che l'update non lanci eccezioni
        assertDoesNotThrow(() -> ControllerInput.ButtonState.update());
    }
}
