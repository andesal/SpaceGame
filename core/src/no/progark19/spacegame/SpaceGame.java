package no.progark19.spacegame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.screens.LoadingScreen;
import no.progark19.spacegame.screens.LobbyScreen;
import no.progark19.spacegame.screens.MainMenuScreen;
import no.progark19.spacegame.screens.PlayScreen;
import no.progark19.spacegame.screens.SettingsScreen;


public class SpaceGame extends Game {
    //public static final String TITLE = "SpaceGame";
    public static final int WIDTH = 480;
    public static final int HEIGHT = 720;

    public OrthographicCamera camera;
    public SpriteBatch batch;

    //public BitmapFont font24;
    public AssetManager assets;

    public LoadingScreen loadingScreen;
    public MainMenuScreen mainMenuScreen;
    public LobbyScreen lobbyScreen;
    public PlayScreen playScreen;
    public SettingsScreen settingsScreen;

    public EntityManager entityManager;


    @Override
    public void create() {
        assets = new AssetManager();
        camera = new OrthographicCamera();
        //camera.setToOrtho(false, WIDTH, HEIGHT);
        batch = new SpriteBatch();

        loadingScreen = new LoadingScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        lobbyScreen = new LobbyScreen(this);
        playScreen = new PlayScreen(this);
        settingsScreen = new SettingsScreen(this);
        this.setScreen(loadingScreen);
    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }


    @Override
    public void dispose() {
        batch.dispose();
        assets.dispose();
        loadingScreen.dispose();
        mainMenuScreen.dispose();
        lobbyScreen.dispose();
        playScreen.dispose();
        settingsScreen.dispose();

    }

}