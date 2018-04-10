package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by anderssalvesen on 10.04.2018.
 */

public class SoundComponent implements Component {

    public Sound sound;

    public SoundComponent() {

    }

    public SoundComponent(String path, float volume, float pitch, float pan) {
        this.sound = Gdx.audio.newSound(Gdx.files.internal(path));
    }

}
