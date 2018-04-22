package no.progark19.spacegame.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.HashMap;
import java.util.Queue;
import java.util.Random;

import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.interfaces.ReceivedDataListener;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.systems.RenderSystem;
import no.progark19.spacegame.utils.ComponentMappers;
import no.progark19.spacegame.utils.EntityFactory;
import no.progark19.spacegame.utils.GameSettings;
import no.progark19.spacegame.utils.MyProgressBar;
import no.progark19.spacegame.utils.Paths;
import no.progark19.spacegame.utils.RenderableWorldState;
import no.progark19.spacegame.utils.json.WorldStateIndexes;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class PlayScreenPilot implements Screen, ReceivedDataListener {

    private final SpaceGame game;
    private Matrix4 debugMatrix;
    private Stage uiStage;
    private Camera uiCamera;
    private Stage overlayStage;

    //private BitmapFont font;
    //private GlyphLayout layout;

    private ShapeRenderer shapeRenderer;
    //private OrthographicCamera camera;
    //private AudioManager audioManager;
    private EntityManager entityManager;
    private PooledEngine engine;
    public Sound theme;
    private EntityFactory entityFactory;

    public MyProgressBar healthBar;
    public MyProgressBar fuelBar;

    public static Label label;


    //- Private methods ----------------------------------------------------------------------------
    private Slider createEngineSlider(final int engineIndex, final float minRot, final float maxRot) {
        Slider engineSlider = new Slider(0, 100, 1f, true, game.skin1);
        engineSlider.setScaleX(3);
        engineSlider.setValue(50);
        engineSlider.addListener(new ChangeListener() {

            float rotDiff = maxRot - minRot;
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String message =  PlayScreenNavigator.MSG_TOKEN_ENGINE_UPDATE + "|"
                                + engineIndex + "|"
                                + (minRot + rotDiff*((Slider) actor).getValue()/100f);

                game.p2pConnector.sendData(message);

                //spaceShip.changeEngineAngle(engineIndex, ((Slider) actor).getValue());
                //FIXME dette er muligens en litt dårlig løsning på dette [ARH]
                //RelativePositionComponent relposcom = ComponentMappers.RELPOS_MAP.get(engineEntity);
                //ForceApplierComponent fcom = ComponentMappers.FORCE_MAP.get(engineEntity);
                //relposcom.rotation = minRot + rotDiff*((Slider) actor).getValue()/100f;
                //fcom.direction = relposcom.rotation + 90;

                //game.p2pConnector.sendData(jpl);

            }
        });

        engineSlider.addListener(new ClickListener() {
            HashMap<String, Object> values;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //engineEntity.add(new ForceOnComponent());
                String message =  PlayScreenNavigator.MSG_TOKEN_ENGINE_ON + "|"
                        + engineIndex  + "|"
                        + "true";

                game.p2pConnector.sendData(message);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                String message =  PlayScreenNavigator.MSG_TOKEN_ENGINE_ON + "|"
                        + engineIndex  + "|"
                        + "false";

                game.p2pConnector.sendData(message);
            }
        });

        return engineSlider;
    }
    //----------------------------------------------------------------------------------------------
    public PlayScreenPilot(final SpaceGame game){
        this.game = game;
        game.camera.setToOrtho(false, SpaceGame.WIDTH, SpaceGame.HEIGHT);
        this.uiCamera = new OrthographicCamera();
        this.uiStage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, uiCamera));
        this.shapeRenderer = new ShapeRenderer();
        this.overlayStage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, uiCamera));


        engine = new PooledEngine();

        entityFactory = new EntityFactory(game, engine);
        entityManager = new EntityManager(engine, entityFactory);


        /*Label healthLabel = new Label("Health", game.skin1);
        healthLabel.setPosition(135, SpaceGame.HEIGHT - 26);
        uiStage.addActor(healthLabel);

        Label fuelLabel = new Label("Fuel", game.skin1);
        fuelLabel.setPosition(135, SpaceGame.HEIGHT - 41);
        uiStage.addActor(fuelLabel);*/

        //Add engine systems
        engine.addSystem(new RenderSystem(game, uiStage));
        //engine.addSystem(new AnimationSystem(game));
        //engine.addSystem(new SweepSystem());
        //engine.addSystem(new NetworkSystem(GameSettings.WORLDSYNCH_REFRESH_RATE, game.p2pConnector));
        engine.addEntityListener(entityManager);

        Table table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        table.top().left();
        table.row().height(SpaceGame.HEIGHT/2 + 70);

        table.add(
                createEngineSlider(0, 360,270)
        ).width((SpaceGame.WIDTH- 40)/4).padLeft(20).padTop(20);
        table.add(
                createEngineSlider(1, 270, 180)
        ).width((SpaceGame.WIDTH- 40)/4).padTop(20);

        table.add(
                createEngineSlider(2, 0, 90)
        ).width((SpaceGame.WIDTH- 40)/4).padTop(20);
        table.add(
                createEngineSlider(3, 90,180)
        ).width((SpaceGame.WIDTH- 40)/4).padTop(20);

        uiStage.addActor(table);

        //this.font = new BitmapFont();
        //FOR ENGINE OPERATOR PLAYER
        /*final Texture imageUp = new Texture("img/fire_button.png");
        final Texture imageDown = new Texture("img/ice_button.png");
        final TextureRegion regionUp = new TextureRegion(imageUp);
        final TextureRegion regionDown = new TextureRegion(imageDown);

        final TextureRegionDrawable trDrawUp = new TextureRegionDrawable(regionUp);
        final TextureRegionDrawable trDrawDown = new TextureRegionDrawable(regionDown);

        final ImageButton elementButton = new ImageButton(trDrawUp, trDrawDown);
        final Sound sound = game.assetManager.get(Paths.SOUND_CHECKBOX_CLICK);
        elementButton.addListener(new ClickListener(){
            boolean toggleState = true;
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (GameSettings.BULLET_TYPE.equals("FIRE")) {
                    ImageButton.ImageButtonStyle oldStyle = elementButton.getStyle();
                    oldStyle.imageUp = trDrawDown;
                    oldStyle.imageDown = trDrawDown;
                    elementButton.setStyle(oldStyle);
                    GameSettings.BULLET_TYPE = "ICE";
                    sound.play(0.1f * GameSettings.EFFECTS_VOLUME);
                } else  {
                    ImageButton.ImageButtonStyle oldStyle = elementButton.getStyle();
                    oldStyle.imageUp = trDrawUp;
                    oldStyle.imageDown = trDrawUp;
                    elementButton.setStyle(oldStyle);
                    GameSettings.BULLET_TYPE = "FIRE";
                    sound.play(0.3f * GameSettings.EFFECTS_VOLUME);
                }
            }
        });
        elementButton.setPosition(10, 20);
        uiStage.addActor(elementButton);*/
    }


    @Override
    public void show() {


        System.out.println("PLAY SCREEN");
        Gdx.input.setInputProcessor(uiStage);

        game.p2pConnector.addReceivedDataListener(this);
    }

    @Override
    public void render(float delta) {
        switch (GameSettings.GAME_STATE) {
            case 0: //Play state
               updateRunning(delta);
                break;
            case 1:
                //updatePause();
                break;
            case 2: // Game over
                //overlayStage.draw();
                //uiStage.draw();
                game.p2pConnector.disconnect();
                dispose();
                game.setScreen(new GameOverScreen(game));
                break;
        }


    }

    private void updateRunning(float deltaTime) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        //entityManager.update();

        engine.update(deltaTime);
        //Draw Ui
        //FIXME skal dette være i et ESC system?
        game.batch.setProjectionMatrix(uiCamera.combined);


        //uiStage.act(Gdx.graphics.getDeltaTime());
        //uiStage.draw();

        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();

        game.batch.end();

        //Draw physics debug info
        if(GameSettings.BOX2D_DRAWDEBUG){
            debugMatrix = game.camera.combined.cpy().scale(
                    GameSettings.BOX2D_PIXELS_TO_METERS,
                    GameSettings.BOX2D_PIXELS_TO_METERS,
                    0);
        }
    }

    private void updatePause() {

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
        uiStage.dispose();
        shapeRenderer.dispose();
        if (theme != null){
            theme.dispose();
        }
    }

    private Object lock = new Object();
    private Queue<RenderableWorldState> worldStateQueue
            = new CircularFifoQueue<RenderableWorldState>(GameSettings.WORLDSYNC_MAXSTATES);
    private Thread worldStateWorker = new Thread((new Runnable() {
        @Override
        public void run() {
            RenderableWorldState currentState;
            try {
                while (game.p2pConnector.hasConnection()){
                    synchronized (lock){
                        while (worldStateQueue.peek() == null) {
                                lock.wait();
                        }
                        currentState = worldStateQueue.poll();
                    }
                    for (float[] state: currentState.getStates()){
                        Entity e = EntityManager.getEntity((int) state[WorldStateIndexes.WS_ENTITYID]);
                        PositionComponent pcom = ComponentMappers.POS_MAP.get(e);
                        VelocityComponent vcom = ComponentMappers.VEL_MAP.get(e);

                        pcom.x        = state[WorldStateIndexes.WS_RENDERABLE_POSX];
                        pcom.y        = state[WorldStateIndexes.WS_RENDERABLE_POSY];
                        pcom.rotation = state[WorldStateIndexes.WS_RENDERABLE_ROTATION];

                        vcom.velx     = state[WorldStateIndexes.WS_RENDERABLE_VX];
                        vcom.vely     = state[WorldStateIndexes.WS_RENDERABLE_VY];
                        vcom.velAngle = state[WorldStateIndexes.WS_RENDERABLE_VR];
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }));


    @Override
    public synchronized void onReceive(RenderableWorldState data) {
        System.out.println("Got data");

    }

    @Override
    public void onReceive(String data) {
        if (data.equals("gameover")){
            game.p2pConnector.disconnect();
            uiStage.clear();
            GameSettings.GAME_STATE = 2;
        }
    }

    private void drawProgressBars() {


    }

    /*
    private void gameOver(){
        Group overGroup = new Group();
        TextButton mainMenu;

        mainMenu = new TextButton("Main Menu", game.skin2, "default");
        mainMenu.setPosition(110, 300);
        mainMenu.setSize(220, 40);
        mainMenu.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                GameSettings.GAME_STATE = 0;
                engine.removeAllEntities();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        overGroup.addActor(mainMenu);

        uiStage.addActor(overGroup);
    }
    */



}