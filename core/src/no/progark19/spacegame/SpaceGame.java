package no.progark19.spacegame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.progark19.spacegame.managers.EntityManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import no.progark19.spacegame.gameObjects.SpaceShip;
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
	private Skin skin;

    public LoadingScreen loadingScreen;
    public MainMenuScreen mainMenuScreen;
    public LobbyScreen lobbyScreen;
    public PlayScreen playScreen;
    public SettingsScreen settingsScreen;

    public EntityManager entityManager;

    public Vector3 translateScreenCoordinates(Vector3 coordinates){
        return camera.unproject(coordinates.add(0, SpaceGame.HEIGHT - 1, 0));
    }

    public Skin getSkin(){
        return skin;
    }

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

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    }

	@Override
	public void render() {
		super.render();
		//FIXME tror det er bedre å bruke interrupts istedenfor å polle? [AndersRahu]
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