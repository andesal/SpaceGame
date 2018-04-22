package no.progark19.spacegame.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import no.progark19.spacegame.systems.CollisionSystem;

import java.util.Queue;

import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.interfaces.ReceivedDataListener;
import no.progark19.spacegame.systems.SpawnSystem;
import no.progark19.spacegame.systems.SweepSystem;
import no.progark19.spacegame.systems.UpdateSystem;
import no.progark19.spacegame.utils.EntityFactory;
import no.progark19.spacegame.utils.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.ForceOnComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.ComponentMappers;
import no.progark19.spacegame.systems.ForceApplierSystem;
import no.progark19.spacegame.systems.MovementSystem;
import no.progark19.spacegame.systems.RenderSystem;
import no.progark19.spacegame.utils.MyProgressBar;
import no.progark19.spacegame.utils.Paths;
import no.progark19.spacegame.utils.RenderableWorldState;
import no.progark19.spacegame.utils.json.WorldStateIndexes;



import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class PlayScreenNavigator implements Screen, ReceivedDataListener {

    private final SpaceGame game;
    private final Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private Stage uiStage;
    private Stage overlayStage;
    private Camera uiCamera;

    private boolean isGameOver;

    private ShapeRenderer shapeRenderer;
    private EntityManager entityManager;
    private PooledEngine engine;
    public Sound theme;
    private EntityFactory entityFactory;

    private MyProgressBar healthBar;
    private MyProgressBar fuelBar;

    public static Label label;

    private Entity[] engines = new Entity[4];


    //- Private methods ----------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    public PlayScreenNavigator(final SpaceGame game){
        GameSettings.BOX2D_PHYSICSWORLD = new World(new Vector2(0,0), true);
        System.out.println("B2D WORLD INITIALIZED");

        this.game = game;
        game.camera.setToOrtho(false, SpaceGame.WIDTH, SpaceGame.HEIGHT);
        this.uiCamera = new OrthographicCamera();
        this.uiStage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, uiCamera));
        this.overlayStage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, uiCamera));
        this.shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawAABBs(true);
        debugRenderer.setDrawVelocities(true);
        engine = new PooledEngine();

        entityFactory = new EntityFactory(game, engine);
        entityManager = new EntityManager(engine, entityFactory);


        // Health and fuel bars
        healthBar = new MyProgressBar(100, 10, Color.RED);
        healthBar.setPosition(40, Gdx.graphics.getHeight() - 20);
        healthBar.setValue((float) GameSettings.START_HEALTH / 100);
        uiStage.addActor(healthBar);

        Label healthLabel = new Label("Health", game.skin1);
        healthLabel.setPosition(145, SpaceGame.HEIGHT - 26);
        uiStage.addActor(healthLabel);

        fuelBar = new MyProgressBar(100, 10, Color.GREEN);
        fuelBar.setPosition(40, Gdx.graphics.getHeight() - 35);
        fuelBar.setValue(GameSettings.START_FUEL / 100);
        uiStage.addActor(fuelBar);

        Label fuelLabel = new Label("Fuel", game.skin1);
        fuelLabel.setPosition(145, SpaceGame.HEIGHT - 41);
        uiStage.addActor(fuelLabel);

        //Add engine systems
        engine.addSystem(new RenderSystem(game, uiStage));
        engine.addSystem(new SpawnSystem(game, entityFactory));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new ForceApplierSystem(game));
        engine.addSystem(new CollisionSystem(game));
        engine.addSystem(new SweepSystem());
        engine.addSystem(new UpdateSystem(game, entityFactory));
        engine.addEntityListener(entityManager);
        engine.addSystem(new ForceApplierSystem(game));

        //Create entities
        final Entity shipEntity = entityFactory.createBaseSpaceShip(uiStage);
        engine.addEntity(shipEntity);

        engines[0] = entityFactory.createShipEngine(
                -23,-50, 315, shipEntity);
        engines[1] = entityFactory.createShipEngine(
                -23,50, 225, shipEntity);
        engines[2] = entityFactory.createShipEngine(
                23,-50, 45, shipEntity);
        engines[3] = entityFactory.createShipEngine(
                23,50, 135, shipEntity);


        engine.addEntity(engines[0]);
        engine.addEntity(engines[1]);
        engine.addEntity(engines[2]);
        engine.addEntity(engines[3]);


        //this.font = new BitmapFont();
        //FOR ENGINE OPERATOR PLAYER
        final Texture imageUp = new Texture("img/fire_button.png");
        final Texture imageDown = new Texture("img/ice_button.png");
        final TextureRegion regionUp = new TextureRegion(imageUp);
        final TextureRegion regionDown = new TextureRegion(imageDown);

        final TextureRegionDrawable trDrawUp = new TextureRegionDrawable(regionUp);
        final TextureRegionDrawable trDrawDown = new TextureRegionDrawable(regionDown);

        final ImageButton elementButton = new ImageButton(trDrawUp, trDrawDown);
        final Sound sound = game.assetManager.get(Paths.SOUND_CHECKBOX_CLICK);
        elementButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (GameSettings.BULLET_TYPE.equals("FIRE")) {
                    ImageButton.ImageButtonStyle oldStyle = elementButton.getStyle();
                    oldStyle.imageUp = trDrawDown;
                    oldStyle.imageDown = trDrawDown;
                    elementButton.setStyle(oldStyle);
                    GameSettings.BULLET_TYPE = "ICE";
                    sound.play(0.3f * GameSettings.EFFECTS_VOLUME);
                } else {
                    ImageButton.ImageButtonStyle oldStyle = elementButton.getStyle();
                    oldStyle.imageUp = trDrawUp;
                    oldStyle.imageDown = trDrawUp;
                    elementButton.setStyle(oldStyle);
                    GameSettings.BULLET_TYPE = "FIRE";
                    sound.play(0.3f * GameSettings.EFFECTS_VOLUME);

                }
            }
        });
        elementButton.setPosition(30, 20);
        uiStage.addActor(elementButton);

    }
    @Override
    public void show() {

        System.out.println("PLAY SCREEN");
        Gdx.input.setInputProcessor(uiStage);

        game.p2pConnector.addReceivedDataListener(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        uiStage.act(Gdx.graphics.getDeltaTime());
        //entityManager.update();


        switch (GameSettings.GAME_STATE) {
            case 0: // Game running
                updateRunning(delta);
                uiStage.draw();
                break;
            case 1: // Game paused
                //pauseGame();
                //overlayStage.draw();
                //uiStage.draw();
                break;
            case 2: // Game over
                game.p2pConnector.sendData("gameover");
                game.p2pConnector.disconnect();
                dispose();
                game.setScreen(new GameOverScreen(game));
                break;
        }
        game.batch.end();
    }

    private void updateRunning(float deltaTime) {
        engine.update(deltaTime);
        //Draw Ui
        //FIXME skal dette være i et ESC system?
        game.batch.setProjectionMatrix(uiCamera.combined);

        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();

        //DEBUG
        //uiStage.act(Gdx.graphics.getDeltaTime());
        //uiStage.draw();

        //Draw physics debug info
        /*
        if(GameSettings.BOX2D_DRAWDEBUG){
            debugMatrix = game.camera.combined.cpy().scale(
                    GameSettings.BOX2D_PIXELS_TO_METERS,
                    GameSettings.BOX2D_PIXELS_TO_METERS,
                    0);
            debugRenderer.render(GameSettings.BOX2D_PHYSICSWORLD, debugMatrix);
        }
        */
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
        debugRenderer.dispose();
        shapeRenderer.dispose();
        GameSettings.BOX2D_PHYSICSWORLD.dispose();
        engine.removeAllEntities();
    }

    private Object lock = new Object();
    private Queue<RenderableWorldState> worldStateQueue
            = new CircularFifoQueue<RenderableWorldState>(GameSettings.WORLDSYNC_MAXSTATES);
    private Thread worldStateWorker = new Thread((new Runnable() {
        @Override
        public void run() {
            RenderableWorldState currentState;
            try {
                while (game.p2pConnector.hasConnection()) {
                    synchronized (lock) {
                        while (worldStateQueue.peek() == null) {
                            lock.wait();
                        }
                        currentState = worldStateQueue.poll();
                    }
                    for (float[] state : currentState.getStates()) {
                        Entity e = EntityManager.getEntity((int) state[WorldStateIndexes.WS_ENTITYID]);
                        PositionComponent pcom = ComponentMappers.POS_MAP.get(e);
                        VelocityComponent vcom = ComponentMappers.VEL_MAP.get(e);

                        pcom.x = state[WorldStateIndexes.WS_RENDERABLE_POSX];
                        pcom.y = state[WorldStateIndexes.WS_RENDERABLE_POSY];
                        pcom.rotation = state[WorldStateIndexes.WS_RENDERABLE_ROTATION];

                        vcom.velx = state[WorldStateIndexes.WS_RENDERABLE_VX];
                        vcom.vely = state[WorldStateIndexes.WS_RENDERABLE_VY];
                        vcom.velAngle = state[WorldStateIndexes.WS_RENDERABLE_VR];
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }));

    static final String MSG_TOKEN_ENGINE_UPDATE = "engineUpdate";
    static final String MSG_TOKEN_ENGINE_ON = "engineOn";

    @Override
    public synchronized void onReceive(RenderableWorldState data) {
        System.out.println("Got data");

        for (float[] state: data.getStates()){
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

    @Override
    public void onReceive(String data) {
        System.out.println("Received:" + data);
        String[] msg = data.split("\\|");

        if (msg[0].equals(MSG_TOKEN_ENGINE_UPDATE)){
            Entity e = engines[Integer.valueOf(msg[1])];
            RelativePositionComponent rpcom = ComponentMappers.RELPOS_MAP.get(e);
            ForceApplierComponent facom  = ComponentMappers.FORCE_MAP.get(e);
            float rotation = Float.valueOf(msg[2]);
            rpcom.rotation = rotation;
            facom.direction = rpcom.rotation + 90;

        } else if (msg[0].equals(MSG_TOKEN_ENGINE_ON)){
            Entity e = engines[Integer.valueOf(msg[1])];
            if(msg[2].equals("false")) {
                e.remove(ForceOnComponent.class);
            } else {
                e.add(engine.createComponent(ForceOnComponent.class));
            }

        } else {
                System.out.println("String tag not understood!");
        }
    }
}