package no.progark19.spacegame.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.screens.SettingsScreen;

/**
 * Created by anderssalvesen on 10.04.2018.
 */

public class AudioManager {

    private boolean gameStarted = false;
    public Sound theme;
    public Sound spaceship;
    public Sound explosion1;
    private SettingsScreen settingsScreen;
    private GameSettings settings;

    public AudioManager() {

        theme = Gdx.audio.newSound(Gdx.files.internal("data/theme.mp3"));
        spaceship = Gdx.audio.newSound(Gdx.files.internal("data/engine.mp3"));
        explosion1 = Gdx.audio.newSound(Gdx.files.internal("data/explosion1.mp3"));
    }

    public void playSound(Sound sound, float volume, float pitch, float pan, boolean loop) {
        sound.play(volume, pitch, pan);
        if (loop) sound.loop();

    }

}
