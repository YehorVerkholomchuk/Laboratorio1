package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe SaveRenderer
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class SaveRenderer implements Common {
    private static final String PATH = "save.txt";

    /**
     * Salva il record migliore se il punteggio corrente è superiore.
     *
     * @param food quantità di cibo raccolto
     * @param lvl livello raggiunto
     * @param diff difficoltà della partita
     * @param time tempo totale della partita
     */
    public void trySave(int food, int lvl, String diff, float time) {
        int[] best = load();

        if (food >= best[0] && lvl >= best[1] && (int) time <= best[3]) {
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            String content =
                    "FOOD=" + food + "\n" +
                    "LVL=" + lvl + "\n" +
                    "DIFF=" + diff + "\n" +
                    "TIME=" + (int) time + "\n" +
                    "DATE=" + date;

            try {
                Gdx.files.local(PATH).writeString(content, false);
            } catch (Exception e) {
                Gdx.app.error("Save", "Failed to write save: " + e.getMessage());
            }
        }
    }

    /**
     * Carica il record salvato come valori numerici.
     *
     * @return array contenente food, livello, difficoltà convertita e tempo
     */
    public int[] load() {
        FileHandle f = Gdx.files.local(PATH);

        if (!f.exists()) return new int[]{0, 0, 0, 0};

        int food = 0, lvl = 0, time = 0;
        String diff = "NORMAL";

        try {
            for (String line : f.readString().split("\\r?\\n")) {
                if (line.startsWith("FOOD=")) food = parse(line);
                else if (line.startsWith("LVL=")) lvl = parse(line);
                else if (line.startsWith("DIFF=")) diff = line.split("=")[1].trim();
                else if (line.startsWith("TIME=")) time = parse(line);
            }
        } catch (Exception e) {
            Gdx.app.error("Save", "Failed to read save: " + e.getMessage());
        }

        return new int[]{food, lvl, diffToScore(diff), time};
    }

    /**
     * Carica il record salvato come stringhe.
     *
     * @return array contenente food, livello, difficoltà, tempo e data
     */
    public String[] loadStrings() {
        FileHandle f = Gdx.files.local(PATH);

        if (!f.exists()) return new String[]{"0", "0", "NORMAL", "0", "-"};

        String food = "0", lvl = "0", diff = "NORMAL", time = "0", date = "-";

        try {
            for (String line : f.readString().split("\\r?\\n")) {
                if (line.startsWith("FOOD=")) food = val(line);
                else if (line.startsWith("LVL=")) lvl = val(line);
                else if (line.startsWith("DIFF=")) diff = val(line);
                else if (line.startsWith("TIME=")) time = val(line);
                else if (line.startsWith("DATE=")) date = val(line);
            }
        } catch (Exception e) {
            Gdx.app.error("Save", "Failed to read save strings: " + e.getMessage());
        }

        return new String[]{food, lvl, diff, time, date};
    }

    /**
     * Estrae il valore dopo il simbolo "=".
     *
     * @param line riga del file di salvataggio
     * @return il valore estratto
     */
    private String val(String line) {
        String[] parts = line.split("=", 2);
        return parts.length > 1 ? parts[1].trim() : "";
    }

    /**
     * Converte una stringa numerica in intero.
     *
     * @param line riga contenente il numero
     * @return il numero convertito oppure 0 in caso di errore
     */
    private int parse(String line) {
        try { return Integer.parseInt(val(line)); }
        catch (NumberFormatException e) { return 0; }
    }

    /**
     * Converte la difficoltà in un punteggio numerico.
     *
     * @param diff livello di difficoltà
     * @return valore numerico associato alla difficoltà
     */
    private int diffToScore(String diff) {
        switch (diff) {
            case "EASY": return 1;
            case "HARD": return 3;
            case "EXTREME": return 4;
            default: return 2;
        }
    }
}
