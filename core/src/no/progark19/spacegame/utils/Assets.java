package no.progark19.spacegame.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import no.progark19.spacegame.GameSettings;


public class Assets {


    public AssetManager manager = new AssetManager();

    public void loadDebugThings() {
        manager.load(Paths.DEBUG_FORCEARROW_TEXTURE_PATH, Texture.class);
    }

    public void loadTextures() {
        manager.load(Paths.SPACESHIP_TEXTURE_PATH, Texture.class);
        manager.load(Paths.ENGINE_TEXTURE_PATH, Texture.class);
        manager.load(Paths.BACKGROUND_TEXTURE_PATH, Texture.class);
        manager.load(Paths.ASTEROID_FIRE_TEXTURE_PATH, Texture.class);
        manager.load(Paths.ASTEROID_ICE_TEXTURE_PATH, Texture.class);
        manager.load(Paths.FIRE_BULLET_TEXTURE_PATH, Texture.class);
        manager.load(Paths.ICE_BULLET_TEXTURE_PATH, Texture.class);
        manager.load(Paths.FUEL_TEXTURE_PATH, Texture.class);
        manager.load(Paths.HEALT_TEXTURE_PATH, Texture.class);
    }

    public void loadTextureAtlases() {
        manager.load(Paths.FIRE_EXPLOSION_2_ATLAS, TextureAtlas.class);
        manager.load(Paths.ICE_EXPLOSION_ATLAS, TextureAtlas.class);

    }

    public void loadSkins() {

    }

    public void loadSounds() {

    }

    public void loadMusic() {

    }







}
