package no.progark19.spacegame;

import no.progark19.spacegame.interfaces.P2pConnector;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import no.progark19.spacegame.screens.LoadingScreen;
import no.progark19.spacegame.screens.MainMenuScreen;
import no.progark19.spacegame.screens.SettingsScreen;
import no.progark19.spacegame.utils.Assets;
import no.progark19.spacegame.utils.Paths;


public class SpaceGame extends Game {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 720;

	public OrthographicCamera camera;
	public SpriteBatch batch;

	//public BitmapFont font24;
	public AssetManager assetManager;
	public Assets assets;

    //FIXME trur disse er unødvendig
	public LoadingScreen loadingScreen;
    public MainMenuScreen mainMenuScreen;
    //public LobbyScreen lobbyScreen;
    //public PlayScreen playScreen;
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

	public Skin skin1, skin2;

	@Override
	public void create() {
		assets = new Assets();
		assetManager = assets.manager;
		assets.loadTextureAtlases();
		assets.loadDebugThings();
		assets.loadTextures();
		assets.loadSkins();
		while (! assetManager.update()) {
			System.out.println("LOADING");
		}
		camera = new OrthographicCamera();
		//camera.setToOrtho(false, WIDTH, HEIGHT);
		batch = new SpriteBatch();

		loadingScreen = new LoadingScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		//lobbyScreen = new LobbyScreen(this);
        //playScreen = new PlayScreen(this);
		settingsScreen = new SettingsScreen(this);
		this.setScreen(loadingScreen);

        skin1 = assetManager.get(Paths.SKIN_1_JSON, Skin.class);
		skin2 = assetManager.get(Paths.SKIN_2_JSON, Skin.class);
		skin1.addRegions(assetManager.get(Paths.SKIN_1_ATLAS, TextureAtlas.class));
		skin2.addRegions(assetManager.get(Paths.SKIN_2_ATLAS, TextureAtlas.class));


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
        assetManager.dispose();
		loadingScreen.dispose();
		mainMenuScreen.dispose();
		//lobbyScreen.dispose();
		//playScreen.dispose();
		settingsScreen.dispose();


    }

}