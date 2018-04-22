package no.progark19.spacegame.screens;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.utils.Paths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class MainMenuScreen implements Screen {

    private static final int LOGO_WIDTH = 260;
    private static final int LOGO_HEIGHT = 150;
    private static final int LOGO_Y = 500;

    private final SpaceGame game;

    private Stage stage;

    private TextButton buttonPlay, buttonExit, buttonOptions;


    private ShapeRenderer shapeRenderer;

    public MainMenuScreen(final SpaceGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        System.out.println("MAIN MENU");
        Gdx.input.setInputProcessor(stage);
        stage.clear();
        initButtons();
    }

    public void update(float delta){
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();

        stage.getBatch().draw(game.assetManager.get(Paths.BACKGROUND_MAIN_MENU_TEXTURE_PATH, Texture.class), 0, 0, SpaceGame.WIDTH, SpaceGame.HEIGHT);
        stage.getBatch().draw(game.assetManager.get(Paths.MENU_TEXT_TEXTURE_PATH, Texture.class), SpaceGame.WIDTH / 2 - LOGO_WIDTH / 2, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);
        update(delta);

        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }

    private void initButtons(){
        final Sound s = game.assetManager.get(Paths.SOUND_CLICK);
        TextButton buttonPlay, buttonExit, buttonOptions;
        buttonPlay = new TextButton("Start Game", game.skin2, "default");
        buttonPlay.setPosition(110, 330);
        buttonPlay.setSize(280, 60);
        buttonPlay.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                s.play(0.1f);
                Music mainTheme = game.assetManager.get(Paths.MUSIC_MAIN_THEME, Music.class);
                mainTheme.setLooping(true);
                mainTheme.setVolume(1f * GameSettings.MUSIC_VOLUME);
                mainTheme.play();
                //TODO GO to lobby instead
                game.setScreen(new LobbyScreen(game));
                //game.setScreen((new PlayScreenPilot(game)));
            }
        });

        buttonOptions = new TextButton("Settings", game.skin2, "default");
        buttonOptions.setPosition(110, 260);
        buttonOptions.setSize(280, 60);
        buttonOptions.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                s.play(0.1f);
                game.setScreen(new SettingsScreen(game));
            }
        });

        buttonExit = new TextButton("Exit", game.skin2, "default");
        buttonExit.setPosition(110, 120);
        buttonExit.setSize(280, 60);
        buttonExit.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(buttonPlay);
        stage.addActor(buttonOptions);
        stage.addActor(buttonExit);
    }
}