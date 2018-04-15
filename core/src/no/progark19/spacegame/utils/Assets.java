package no.progark19.spacegame.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by anderssalvesen on 15.04.2018.
 */

public class Assets {

    private AssetManager assetManager;

    public Assets() {
        this.assetManager = new AssetManager();
        loadAudio();
    }

    public AssetManager getAssetManager(){
        return this.assetManager;
    }


    public void loadAudio() {
        assetManager.load("sounds/theme.wav", Music.class);
        assetManager.load("sounds/explosion1.mp3", Sound.class);
    }

    public void loadTextures() {

    }

    public Sound getSound(String asset) {
        return assetManager.get(asset, Sound.class);
    }

    public Texture getTexture(String asset) {
        return  assetManager.get(asset, Texture.class);
    }

    public void dispose() {
        assetManager.clear(); // gets rid of all assets, but keeps the manager
        //assetManager.dispose(); kills the manager
    }

    public static abstract class SoundAsset {
        public static final String EXPLOSION1 = "sounds/explosion1.mp3";
    }

    public static abstract class MusicAsset {
        public static final String MAINTHEME = "sounds/theme.wav";
    }

}
