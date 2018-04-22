package no.progark19.spacegame.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import no.progark19.spacegame.systems.AnimationSystem;
import no.progark19.spacegame.systems.CollisionSystem;

import java.util.HashMap;
import java.util.Queue;
import java.util.Random;

import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.interfaces.ReceivedDataListener;
import no.progark19.spacegame.systems.NetworkSystem;
import no.progark19.spacegame.systems.SpawnSystem;
import no.progark19.spacegame.systems.SweepSystem;
import no.progark19.spacegame.systems.UpdateSystem;
import no.progark19.spacegame.utils.Assets;
import no.progark19.spacegame.utils.EntityFactory;
import no.progark19.spacegame.utils.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.ForceOnComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.ComponentMappers;
import no.progark19.spacegame.systems.ControlSystem;
import no.progark19.spacegame.systems.ForceApplierSystem;
import no.progark19.spacegame.systems.MovementSystem;
import no.progark19.spacegame.systems.RenderSystem;
import no.progark19.spacegame.utils.MyProgressBar;
import no.progark19.spacegame.utils.Paths;
import no.progark19.spacegame.utils.RenderableWorldState;
import no.progark19.spacegame.utils.json.JsonPayload;
import no.progark19.spacegame.utils.json.JsonPayloadTags;
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

    private boolean isPause;
    private boolean isGameOver;

    private Group pauseGroup;


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
        healthBar.setValue((float) GameSettings.START_HEALTH/100);
        uiStage.addActor(healthBar);

        Label healthLabel = new Label("Health", game.skin1);
        healthLabel.setPosition(145, SpaceGame.HEIGHT - 26);
        uiStage.addActor(healthLabel);

        fuelBar = new MyProgressBar(100, 10, Color.GREEN);
        fuelBar.setPosition(40, Gdx.graphics.getHeight() - 35);
        fuelBar.setValue(GameSettings.START_FUEL/100);
        uiStage.addActor(fuelBar);

        Label fuelLabel = new Label("Fuel", game.skin1);
        fuelLabel.setPosition(145, SpaceGame.HEIGHT - 41);
        uiStage.addActor(fuelLabel);

        //Buttons
        TextButton pauseButton;
        pauseButton =  new TextButton("Pause", game.skin2, "default");
        pauseButton.setSize(60,30);
        pauseButton.setPosition(400, SpaceGame.HEIGHT - 41);
        pauseButton.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.GAME_STATE = 1;
            }
        });
        uiStage.addActor(pauseButton);


        //Add engine systems
        engine.addSystem(new ControlSystem(game, entityFactory));
        engine.addSystem(new RenderSystem(game, uiStage));
        engine.addSystem(new SpawnSystem(game, entityFactory));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new ForceApplierSystem(game));
        engine.addSystem(new AnimationSystem(game));
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
        elementButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (GameSettings.BULLET_TYPE.equals("FIRE")) {
                    ImageButton.ImageButtonStyle oldStyle = elementButton.getStyle();
                    oldStyle.imageUp = trDrawDown;
                    oldStyle.imageDown = trDrawDown;
                    elementButton.setStyle(oldStyle);
                    GameSettings.BULLET_TYPE = "ICE";
                    sound.play(0.3f * GameSettings.EFFECTS_VOLUME);
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
        elementButton.setPosition(30, 20);
        uiStage.addActor(elementButton);


        //this.layout = new GlyphLayout();

        label = new Label("", game.skin1);
        if (GameSettings.isNavigator) {
            //label.setPosition(50,0);
            //label.setWidth(SpaceGame.WIDTH);
        } else {
            //label.setPosition(0,0);
            //label.setHeight(SpaceGame.HEIGHT);
        }

        /*
        //FOR LOOKOUT PLAYER
        //TODO UPDATE LABEL COORDINATES
        label = new Label("LALALA", game.skin1);
        label.setPosition(0, 0);
        //label.setPosition(game.translateScreenCoordinates(new Vector3(40,0,0)).x, regionDown.getRegionHeight());
        //FØKKER SEG NÅR MAN ENDRER POSISJON PÅ LABEL

        label.setWidth(SpaceGame.WIDTH);
        label.setHeight(SpaceGame.HEIGHT);
        label.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("ORIGINAL " + x + " : " + y);
                Vector3 tp = game.translateScreenCoordinates(new Vector3(x, y, 0));
                Vector3 sp = game.translateScreenCoordinates(new Vector3(game.camera.position.x, game.camera.position.y - shipTexture.getHeight()/2, 0));
                float dx = tp.x - sp.x;
                float dy = tp.y - sp.y;
                float delta = (float) Math.atan(dy/dx);
                Vector2 velVec = new Vector2(dx, dy);
                if (tp.x < sp.x) {
                    delta += Math.PI;

                }

                //Entity entity = entityFactory.createProjectile(game.camera.position.x, game.camera.position.y, 0,vely, "ICE", 0 );
                //engine.addEntity(entity);
                //BodyComponent bcom = ComponentMappers.BOD_MAP.get(shipEntity);

                //engine.addEntity(entityFactory.createProjectile(game.camera.position.x, game.camera.position.y, 0,100,  GameSettings.BULLET_TYPE, bcom.body.getAngle()));

                /*
                velVec.setAngleRad(delta);
                System.out.println(delta);
                float relativeRotation = ComponentMappers.POS_MAP.get(shipEntity).rotation;
                //engine.addEntity(entityFactory.createProjectile(game.camera.position.x, game.camera.position.y, 0,100,  GameSettings.BULLET_TYPE, relativeRotation));
                Body body = new Body();
                body.get


                return false;

            }
        });
        uiStage.addActor(label);
        */
    }

    @Override
    public void show() {

        System.out.println("PLAY SCREEN");
        Gdx.input.setInputProcessor(uiStage);

        game.p2pConnector.addReceivedDataListener(this);
        //TODO COMMENT OUT THIS
        GameSettings.setRandomSeed((new Random()).nextLong());
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
                pauseGame();
                overlayStage.draw();
                uiStage.draw();
                break;
            case 2: // Game over
                gameOver();
                overlayStage.draw();
                uiStage.draw();
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

        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();

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

    static final String MSG_TOKEN_ENGINE_UPDATE = "engineUpdate";
    static final String MSG_TOKEN_ENGINE_ON = "engineOn";

    @Override
    public synchronized void onReceive(RenderableWorldState data) {
        System.out.println("Got data");
        /*synchronized (lock) {
            worldStateQueue.add(data);
            if (worldStateWorker.getState() == Thread.State.NEW) {
                worldStateWorker.start();
            } else {
                lock.notify();
            }
        }*/

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

        /*int TAG = data.getTAG();
        HashMap<String, Object> values;
        int entityID;
        Entity engineEntity;

        switch (TAG){
            case JsonPayloadTags.ENGINE_ROTATION_UPDATE:
                values = (HashMap<String, Object>) data.getValue();
                entityID = (Integer) values.get(JsonPayloadTags.ENGINE_UPDATE_ENGINEID);
                float rotation = (Float) values.get(JsonPayloadTags.ENGINE_ROTATION_UPDATE_ROTATION);
                float forceDir = (Float) values.get(JsonPayloadTags.ENGINE_ROTATION_UPDATE_FORCEDIRECTION);

                engineEntity = EntityManager.getEntity(entityID);

                RelativePositionComponent relposcom = ComponentMappers.RELPOS_MAP.get(engineEntity);
                ForceApplierComponent fcom = ComponentMappers.FORCE_MAP.get(engineEntity);


                relposcom.rotation = rotation;
                fcom.direction = forceDir;


                break;
            case JsonPayloadTags.ENGINE_ON_UPDATE:
                values = (HashMap<String, Object>) data.getValue();
                entityID = (Integer) values.get(JsonPayloadTags.ENGINE_UPDATE_ENGINEID);
                boolean isOn = (Boolean) values.get(JsonPayloadTags.ENGINE_ON_UPDATE_ISON);

                engineEntity = EntityManager.getEntity(entityID);
                if (isOn) {
                    engineEntity.add(new ForceOnComponent());
                } else {
                    engineEntity.remove(ForceOnComponent.class);
                }

                break;
            case JsonPayloadTags.SYNC_BODY:
                values = (HashMap<String, Object>) data.getValue();
                entityID = (Integer) values.get(JsonPayloadTags.SYNC_ENTITYID);
                rotation = (Float) values.get(JsonPayloadTags.SYNC_ROTATION);
                posX = (Float) values.get(JsonPayloadTags.SYNC_POSX);
                posY = (Float) values.get(JsonPayloadTags.SYNC_POSY);

                PositionComponent pcom = ComponentMappers.POS_MAP.get(EntityManager.getEntity(entityID));
                pcom.rotation = rotation;
                pcom.x = posX;
                pcom.y = posY;

                break;
            default:
                System.out.println("NOT LEGAL JSON TAG");
        }*/


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
    private void pauseGame(){

        pauseGroup = new Group();
        for (Actor actor : uiStage.getActors()) {
            actor.remove();
        }
        final TextButton mainMenu, resumeGame;
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

        resumeGame = new TextButton("Resume Game", game.skin2, "default" );
        resumeGame.setPosition(110, 350);
        resumeGame.setSize(220, 40);
        resumeGame.addAction(sequence(alpha(0), parallel(fadeIn(.5f), moveBy(0, -20, .5f, Interpolation.pow5Out))));
        resumeGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseGroup.removeActor(resumeGame, true);
                pauseGroup.removeActor(mainMenu, true);
                //uiStage.getActors().removeValue(pauseGroup);
                //for (Actor actor : uiStage.getActors()) {
                //    actor.remove();
                //}
                resumeGame();
            }
        });

        pauseGroup.addActor(resumeGame);
        pauseGroup.addActor(mainMenu);
        uiStage.addActor(pauseGroup);
    }

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

    private void resumeGame(){
            pauseGroup.remove();

            GameSettings.GAME_STATE = 0;
    }
}