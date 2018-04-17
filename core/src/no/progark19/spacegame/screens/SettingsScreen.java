package no.progark19.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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
    private Skin skin1;
    private Skin skin2;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private Table table;

    // Todo: Implement global sound
    float volume = 0.5f;
    long soundId = 0;

    boolean clicked = false;

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

        this.skin1 = new Skin(Gdx.files.internal("ui/uiskin.json"));
        this.skin1.add("default-font", game.font24);
        this.skin1.addRegions(new TextureAtlas("ui/uiskin.atlas"));

        this.skin2 = new Skin(Gdx.files.internal("ui/sgx/sgxui.json"));
        this.skin2.add("default-font", game.font24);
        this.skin2.addRegions(new TextureAtlas("ui/sgx/sgxui.atlas"));

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

        final TextButton comBtt, buttonExit;

        final Slider volume = new Slider(1, 100, 1, false, skin2);
        volume.setValue(1);
        final Label volumeValue = new Label("1.0", skin2);
        Table table = new Table();
        final Slider pan = new Slider(-1f, 1f, 0.1f, false, skin2);
        pan.setValue(0);
        final Label panValue = new Label("0.0", skin2);

        volume.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //sound.setVolume(soundId, volume.getValue());
                volumeValue.setText("" + volume.getValue());
            }
        });

        pan.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //sound.setPan(soundId, pan.getValue(), volume.getValue());
                panValue.setText("" + pan.getValue());
            }
        });

        comBtt = new TextButton("Network usage", skin2, "default");
        comBtt.setPosition(110, 170);
        comBtt.setSize(280,60);
        comBtt.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        comBtt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Todo: Make WiFi / Bluetooth toggle
            }
        });

        buttonExit = new TextButton("Main Menu", skin2, "default");
        buttonExit.setPosition(110, 100);
        buttonExit.setSize(280, 60);
        buttonExit.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.setFillParent(true);
        table.padTop(50);
        table.add(new Label("Volume", skin2));
        table.add(volume);
        table.add(volumeValue);

        stage.addActor(table);
        stage.addActor(comBtt);
        stage.addActor(buttonExit);

    }


}
