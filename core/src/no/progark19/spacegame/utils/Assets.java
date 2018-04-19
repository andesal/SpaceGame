package no.progark19.spacegame.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import no.progark19.spacegame.GameSettings;


public class Assets {


    public AssetManager manager = new AssetManager();


    public String fireAsteroidString = GameSettings.FIRE_EXPLOSION;
    private String iceAsteroidString = GameSettings.ICE_EXPLOSION;



    public void loadTextureAtlases() {
        manager.load(fireAsteroidString, TextureAtlas.class);
        manager.load(iceAsteroidString, TextureAtlas.class);

    }

    public void loadSkins() {

    }

    public void loadSounds() {

    }

    public void loadMusic() {

    }

    public void loadTextures() {

    }





}
