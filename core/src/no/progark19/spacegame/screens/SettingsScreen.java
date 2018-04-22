package no.progark19.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import no.progark19.spacegame.utils.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.utils.Paths;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


// Todo: implement: Lyd av/på, bluetooth/wifi toggle,
public class SettingsScreen implements Screen {

    private static final int LOGO_WIDTH = 260;
    private static final int LOGO_HEIGHT = 70;
    private static final int LOGO_Y = 570;

    private final SpaceGame game;
    private Stage stage;
    private ShapeRenderer shapeRenderer;


    // Todo: Implement global sound
    float volume = 0;
    long soundId = 0;


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
        initSettingsBtt();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();

        stage.getBatch().draw(game.assetManager.get(Paths.SETTING_TEXT_TEXTURE_PATH, Texture.class), SpaceGame.WIDTH / 2 - LOGO_WIDTH / 2, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);


        stage.getBatch().end();
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
        final Sound s = game.assetManager.get(Paths.SOUND_CLICK);

        final TextButton comBtt, buttonExit;
        final CheckBox wifi_check = new CheckBox("WiFi", game.skin2);
        final CheckBox bt_check = new CheckBox("Bluetooth", game.skin2);
        ButtonGroup<Button> comGroup = new ButtonGroup<Button>(wifi_check, bt_check);
        comGroup.setMaxCheckCount(1);
        comGroup.setMinCheckCount(1);

        final CheckBox easy_mode = new CheckBox("Easy Mode", game.skin2);
        final CheckBox hard_mode = new CheckBox("Hard Mode", game.skin2);

        ButtonGroup<Button> difficulty_group = new ButtonGroup<Button>(easy_mode, hard_mode);
        difficulty_group.setMaxCheckCount(1);
        difficulty_group.setMinCheckCount(1);

        final Slider volume = new Slider(0, 100, 1, false, game.skin2);
        volume.setValue(100);
        final Label volumeValue = new Label("100", game.skin2);

        final Slider effVolume = new Slider(0, 100, 1, false, game.skin2);
        effVolume.setValue(100);
        final Label effVolumeValue = new Label("100", game.skin2);

        Table table = new Table();
        final Slider pan = new Slider(-1, 100, 1, false, game.skin2);
        pan.setValue(50);
        final Label panValue = new Label("50", game.skin2);

        volume.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //sound.setVolume(soundId, volume.getValue());
                volumeValue.setText("" + volume.getValue());
                GameSettings.MUSIC_VOLUME = volume.getValue()/100;

            }
        });

        effVolume.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //sound.setVolume(soundId, volume.getValue());
                effVolumeValue.setText("" + effVolume.getValue());
                GameSettings.EFFECTS_VOLUME = effVolume.getValue()/100;
            }
        });

        pan.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //sound.setPan(soundId, pan.getValue(), volume.getValue());
                panValue.setText("" + pan.getValue());
            }
        });

        wifi_check.setChecked(true);
        bt_check.setChecked(false);

        easy_mode.setChecked(false);
        hard_mode.setChecked(true);

        final Sound sound = game.assetManager.get(Paths.SOUND_CHECKBOX_CLICK);

        wifi_check.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.graphics.setContinuousRendering(wifi_check.isChecked());
                sound.play(0.1f);
            }
        });

        bt_check.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.graphics.setContinuousRendering(bt_check.isChecked());
                sound.play(0.1f);
            }
        });

        easy_mode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.graphics.setContinuousRendering(easy_mode.isChecked());
                GameSettings.SPACESHIP_ENABLE_ROTATION = false;
                sound.play(0.1f);
            }
        });

        hard_mode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.graphics.setContinuousRendering(hard_mode.isChecked());
                GameSettings.SPACESHIP_ENABLE_ROTATION = true;
                sound.play(0.1f);
            }
        });
        comBtt = new TextButton("Network usage", game.skin2, "default");
        comBtt.setPosition(110, 170);
        comBtt.setSize(280,60);
        comBtt.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        comBtt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Todo: Make WiFi / Bluetooth toggle
            }
        });

        buttonExit = new TextButton("Main Menu", game.skin2, "default");
        buttonExit.setPosition(110, 100);
        buttonExit.setSize(280, 60);
        buttonExit.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                s.play(0.1f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);
        table.setFillParent(true);

        table.padTop(250);
        table.add(new Label("Music volume ", game.skin2));
        table.add(volume);
        table.add(volumeValue);
        table.padBottom(20);
        table.row();
        table.add(new Label("Effects volume ", game.skin2));
        table.add(effVolume);
        table.add(effVolumeValue);


        wifi_check.setPosition(110, 520);
        bt_check.setPosition(110, 500);

        easy_mode.setPosition(110, 350);
        hard_mode.setPosition(110, 330);


        stage.addActor(wifi_check);
        stage.addActor(bt_check);
        stage.addActor(easy_mode);
        stage.addActor(hard_mode);
        stage.addActor(table);
        //stage.addActor(comBtt);
        stage.addActor(buttonExit);
    }
}
