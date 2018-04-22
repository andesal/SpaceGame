package no.progark19.spacegame.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import javafx.scene.shape.Path;

/**
 * Created by anderssalvesen on 15.04.2018.
 */

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
        manager.load(Paths.PILOT_DASHBOARD_BACKGROUND, Texture.class);

        manager.load(Paths.LOBBY_TEXT_TEXTURE_PATH, Texture.class);
        manager.load(Paths.LOBBY_TEXT_TEXTURE_PATH, Texture.class);
        manager.load(Paths.PAUSE_LOGO_PATH, Texture.class);

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
        manager.load(Paths.SOUND_CLICK, Sound.class);
        manager.load(Paths.SOUND_SHOT_FIRED, Sound.class);
        manager.load(Paths.SOUND_COLLISION_SPACESHIP, Sound.class);
        manager.load(Paths.SOUND_ASTEROID_EXPLOSION, Sound.class);
        manager.load(Paths.SOUND_POWERUP, Sound.class);
        manager.load(Paths.SOUND_CHECKBOX_CLICK, Sound.class);

    }

    public void loadMusic() {
        manager.load(Paths.MUSIC_MAIN_THEME, Music.class);
    }

}
