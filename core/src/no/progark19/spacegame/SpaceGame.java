package no.progark19.spacegame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.progark19.spacegame.interfaces.P2pConnector;
import no.progark19.spacegame.managers.EntityManager;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import no.progark19.spacegame.screens.LoadingScreen;
import no.progark19.spacegame.screens.MainMenuScreen;
import no.progark19.spacegame.screens.SettingsScreen;


public class SpaceGame extends Game {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 720;

	public OrthographicCamera camera;
	public SpriteBatch batch;
	ParticleEffect pe;

	public BitmapFont font24;
	public AssetManager assets;
	private Skin skin;

    //FIXME trur disse er unødvendig
	public LoadingScreen loadingScreen;
	public MainMenuScreen mainMenuScreen;
	public LobbyScreen lobbyScreen;
	public PlayScreen playScreen;
	public SettingsScreen settingsScreen;

    public P2pConnector p2pConnector;

	public SpaceGame() {
		p2pConnector = null;
	}

	public SpaceGame(P2pConnector p2pConnector) {
		this.p2pConnector = p2pConnector;
	}

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

		initFonts();

		loadingScreen = new LoadingScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		lobbyScreen = new LobbyScreen(this);
//		playScreen = new PlayScreen(this);
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
		//playScreen.dispose();
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