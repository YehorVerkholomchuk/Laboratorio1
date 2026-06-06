package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe AudioRenderer
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class AudioRenderer implements Disposable {
    private Random random;
    private final ArrayList<Music> mainTracks;
    private final ArrayList<String> mainNames;

    private float startTimer = 0f;
    private final float startDuration = 5f;
    private float gameOverTimer = 0f;
    private final float gameOverDuration = 2f;

    private final Sound eat1Sound;
    private final Sound eat2Sound;
    private final Sound clickSound;
    private final Sound launchSound;
    private final Sound hitSound;
    private final Sound gameOverSound;
    private final Sound levelSound;
    private final Sound bounce1Sound;
    private final Sound bounce2Sound;
    private final Sound startSound;
    private final Sound loseSound;

    private final Music main1Music;
    private final Music main2Music;
    private final Music menuMusic;

    /**
     * Inizializza l'audio e tutto il SFX
     */
    public AudioRenderer() {
        random = new Random();
        mainTracks = new ArrayList<>();
        mainNames = new ArrayList<>();

        eat1Sound = Gdx.audio.newSound(Gdx.files.internal("audio/eat1.wav"));
        eat2Sound = Gdx.audio.newSound(Gdx.files.internal("audio/eat2.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/hit.wav"));
        launchSound = Gdx.audio.newSound(Gdx.files.internal("audio/launch.wav"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/click.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audio/gameOver.wav"));
        levelSound = Gdx.audio.newSound(Gdx.files.internal("audio/level.wav"));
        bounce1Sound = Gdx.audio.newSound(Gdx.files.internal("audio/bounce1.wav"));
        bounce2Sound = Gdx.audio.newSound(Gdx.files.internal("audio/bounce2.wav"));
        startSound = Gdx.audio.newSound(Gdx.files.internal("audio/start.wav"));
        loseSound = Gdx.audio.newSound(Gdx.files.internal("audio/lose.wav"));

        main1Music = Gdx.audio.newMusic(Gdx.files.internal("audio/main1.mp3"));
        main2Music = Gdx.audio.newMusic(Gdx.files.internal("audio/main2.mp3"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menu.mp3"));

        mainTracks.add(main1Music);
        mainTracks.add(main2Music);

        mainNames.add("Kirby Adventure");
        mainNames.add("Super Mario Bros");
    }

    public int getMusicCount() {
        return mainTracks.size();
    }

    public String getMusicName(int index) {
        return mainNames.get(index);
    }

    /**
     * Suona l'audio eat
     */
    public void playEat() {
        eat1Sound.play(1f);
    }

    /**
     * Suona l'audio hit
     */
    public void playHit() {
        hitSound.play(1f);
    }

    /**
     * Suona l'audio launch
     */
    public void playLaunch() {
        launchSound.play(0.6f);
    }

    /**
     * Suona l'audio click
     */
    public void playClick() {
        clickSound.play(1f);
    }

    /**
     * Suona l'audio level up
     */
    public void playLevel() {
        levelSound.play(1f);
    }

    /**
     * Suona l'audio lose
     */
    public void playLose() {
        loseSound.play(0.7f);
    }

    /**
     * Suona l'audio bounce
     */
    public void playBounce() {
        if (random.nextBoolean()) bounce1Sound.play(1f);
        else bounce2Sound.play(1f);
    }

    /**
     * Suona l'audio game over
     */
    public void playGameOver() {
        stopMusic();
        gameOverSound.play(0.7f);
        gameOverTimer = gameOverDuration;
    }

    /**
     * Suona l'audio start
     */
    public void playStart() {
        startSound.play(1f);
        startTimer = startDuration;
    }

    /**
     * Suona la musica principale
     *
     * @param index indice della musica selezionata
     */
    public void playMainMusic(int index) {
        stopMusic();

        Music chosen = mainTracks.get(index);
        chosen.setLooping(true);

        if (chosen.equals(main1Music)) {
            chosen.setVolume(0.5f);
        } else if (chosen.equals(main2Music)) {
            chosen.setVolume(0.8f);
        }

        chosen.play();
    }

    /**
     * Suona la musica del menu
     */
    public void playMenuMusic() {
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5f);
        menuMusic.play();
    }

    /**
     * Ferma tutta la musica
     */
    public void stopMusic() {
        main1Music.stop();
        main2Music.stop();
        menuMusic.stop();
    }

    /**
     * Ferma tutto l'audio e SFX
     */
    public void stopAll() {
        main1Music.stop();
        main2Music.stop();
        menuMusic.stop();

        startSound.stop();
        gameOverSound.stop();
        levelSound.stop();
        bounce1Sound.stop();
        bounce2Sound.stop();
        clickSound.stop();
        launchSound.stop();
        hitSound.stop();
        eat1Sound.stop();
        eat2Sound.stop();
    }

    /**
     * Aggiorna i timer dell'audio
     *
     * @param dt delta time
     * @param menu menu del gioco
     */
    public void update(float dt, Menu menu) {
        if (startTimer > 0) {
            startTimer -= dt;
            if (startTimer <= 0) playMainMusic(menu.getMusicIndex());
        }

        if (gameOverTimer > 0) {
            gameOverTimer -= dt;
            if (gameOverTimer <= 0) playMenuMusic();
        }
    }

    /**
     * Dispose degli elementi
     */
    @Override
    public void dispose() {
        eat1Sound.dispose();
        eat2Sound.dispose();
        hitSound.dispose();
        launchSound.dispose();
        clickSound.dispose();
        gameOverSound.dispose();
        levelSound.dispose();
        bounce1Sound.dispose();
        bounce2Sound.dispose();
        startSound.dispose();
        loseSound.dispose();

        for (Music m : mainTracks) m.dispose();
        menuMusic.dispose();
    }
}
