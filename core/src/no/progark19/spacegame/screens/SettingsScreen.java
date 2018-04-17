package no.progark19.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import no.progark19.spacegame.SpaceGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


// TODO implement: Lyd av/på, bluetooth/wifi toggle,
public class SettingsScreen implements Screen {

    private final SpaceGame game;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;

    public SettingsScreen(SpaceGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        System.out.println("SETTINGS");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = new Skin(Gdx.files.internal("ui/sgx/sgxui.json"));
        this.skin.add("default-font", game.font24);
        this.skin.addRegions(new TextureAtlas("ui/sgx/sgxui.atlas"));

        initSettingsBtt();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    private void initSettingsBtt(){
        TextButton soundBtt, comBtt, buttonExit;

        soundBtt = new TextButton("Sound", skin);
        soundBtt.setPosition(110, 340);
        soundBtt.setSize(280, 60);
        soundBtt.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        soundBtt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Todo: Make sound on / off toggle
                }
        });

        comBtt = new TextButton("Network usage", skin, "default");
        comBtt.setPosition(110, 270);
        comBtt.setSize(280,60);
        comBtt.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        comBtt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Todo: Make WiFi / Bluetooth toggle
            }
        });

        buttonExit = new TextButton("Main Menu", skin, "default");
        buttonExit.setPosition(110, 100);
        buttonExit.setSize(280, 60);
        buttonExit.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(soundBtt);
        stage.addActor(comBtt);
        stage.addActor(buttonExit);

    }

}
