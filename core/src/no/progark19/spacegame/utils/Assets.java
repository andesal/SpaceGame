package no.progark19.spacegame.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


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
        manager.load(Paths.PROGRESSBAR_TEXTURE_PATH, Texture.class);
        manager.load(Paths.BACKGROUND_MAIN_MENU_TEXTURE_PATH, Texture.class);
        manager.load(Paths.MENU_TEXT_TEXTURE_PATH, Texture.class);
        manager.load(Paths.SETTING_TEXT_TEXTURE_PATH, Texture.class);
    }

    public void loadTextureAtlases() {
        manager.load(Paths.FIRE_EXPLOSION_2_ATLAS, TextureAtlas.class);
        manager.load(Paths.ICE_EXPLOSION_ATLAS, TextureAtlas.class);
    }

    public void loadSkins() {

        manager.load(Paths.SKIN_1_JSON, Skin.class);
        manager.load(Paths.SKIN_2_JSON, Skin.class);

        manager.load(Paths.SKIN_1_ATLAS, TextureAtlas.class);
        manager.load(Paths.SKIN_2_ATLAS, TextureAtlas.class);

    }

    public void loadSounds() {

    }

    public void loadMusic() {

    }







}
