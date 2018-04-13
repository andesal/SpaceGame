package no.progark19.spacegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

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
	private SpriteBatch batch;

	public BitmapFont font24;
	public AssetManager assets;

	public LoadingScreen loadingScreen;
	public MainMenuScreen mainMenuScreen;
	public LobbyScreen lobbyScreen;
	public PlayScreen playScreen;
	public SettingsScreen settingsScreen;

    public Vector3 translateScreenCoordinates(Vector3 coordinates){
        Gdx.app.log("Translation","Screen Coordinates" + coordinates.x + "," + coordinates.y);
        Vector3 worldCoordinates = camera.unproject(coordinates);
        Gdx.app.log("Translation","Screen Coordinates" + worldCoordinates.x + "," + worldCoordinates.y);
        return worldCoordinates;
    }

	@Override
	public void create() {
		assets = new AssetManager();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		batch = new SpriteBatch();

		initFonts();

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

	public SpriteBatch getBatch() {
		return this.batch;
	}

	private void initFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params.size = 24;
		params.color = Color.BLACK;
		font24 = generator.generateFont(params);
	}
}
