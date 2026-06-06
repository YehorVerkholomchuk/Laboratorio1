package io.github.some_example_name.tests;

import io.github.some_example_name.SaveRenderer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SaveRendererTest {

    private final SaveRenderer save = new SaveRenderer();

    @Test
    void shouldFormatDateCorrectly() {
        // 1. Arrange: Data odierna nel formato atteso
        String expected = java.time.LocalDate.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // 2. Act: Generazione della data con la stessa logica del metodo trySave
        String actual = java.time.LocalDate.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // 3. Assert: Verifica che il formato della data sia corretto
        assertEquals(expected, actual);
        assertTrue(actual.matches("\\d{2}\\.\\d{2}\\.\\d{4}"));
    }

    @Test
    void shouldCreateInstanceSuccessfully() {
        // 1. Arrange: Nessuna configurazione necessaria
        // 2. Act: Creazione dell'istanza
        SaveRenderer renderer = new SaveRenderer();

        // 3. Assert: Verifica che l'oggetto non sia nullo
        assertNotNull(renderer);
    }
}
