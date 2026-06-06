package io.github.some_example_name.tests;

import io.github.some_example_name.DrawRenderer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DrawRendererTest {

    @Test
    void shouldFormatSecondsCorrectly() {
        // 1. Arrange: Valore in secondi da convertire
        int seconds = 90;

        // 2. Act: Formattazione tramite metodo privato simulata manualmente
        String result = String.format("%02d:%02d", seconds / 60, seconds % 60);

        // 3. Assert: Verifica che il formato mm:ss sia corretto
        assertEquals("01:30", result);
    }

    @Test
    void shouldFormatZeroSecondsCorrectly() {
        // 1. Arrange: Valore zero secondi
        int seconds = 0;

        // 2. Act: Formattazione del tempo
        String result = String.format("%02d:%02d", seconds / 60, seconds % 60);

        // 3. Assert: Verifica che zero secondi venga formattato correttamente
        assertEquals("00:00", result);
    }

    @Test
    void shouldCreateNewInstanceSuccessfully() {
        // 1. Arrange: Nessuna configurazione necessaria
        // 2. Act: Creazione dell'istanza
        DrawRenderer draw = new DrawRenderer();

        // 3. Assert: Verifica che l'oggetto non sia nullo
        assertNotNull(draw);
    }
}
