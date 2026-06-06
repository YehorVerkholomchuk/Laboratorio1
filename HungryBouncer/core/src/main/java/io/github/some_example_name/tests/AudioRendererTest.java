package io.github.some_example_name.tests;

import io.github.some_example_name.AudioRenderer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AudioRendererTest {

    @Test
    void shouldHaveTwoMainTracks() {
        // 1. Arrange: Creazione dell'istanza di AudioRenderer
        AudioRenderer audio = new AudioRenderer();

        // 2. Act: Recupero del numero di tracce musicali
        int count = audio.getMusicCount();

        // 3. Assert: Verifica che ci siano esattamente due tracce principali
        assertEquals(2, count);
    }

    @Test
    void shouldReturnCorrectMusicNames() {
        // 1. Arrange: Creazione dell'istanza di AudioRenderer
        AudioRenderer audio = new AudioRenderer();

        // 2. Act: Recupero dei nomi delle tracce
        String first = audio.getMusicName(0);
        String second = audio.getMusicName(1);

        // 3. Assert: Verifica che i nomi corrispondano a quelli attesi
        assertEquals("Kirby Adventure", first);
        assertEquals("Super Mario Bros", second);
    }

    @Test
    void shouldCreateNewInstanceSuccessfully() {
        // 1. Arrange: Nessuna configurazione necessaria
        // 2. Act: Creazione dell'istanza
        AudioRenderer audio = new AudioRenderer();

        // 3. Assert: Verifica che l'oggetto non sia nullo
        assertNotNull(audio);
    }
}
