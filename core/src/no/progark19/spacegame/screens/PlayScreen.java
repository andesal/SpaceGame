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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.HashMap;
import java.util.Queue;
import java.util.Random;

import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.interfaces.ReceivedDataListener;
import no.progark19.spacegame.systems.AnimationSystem;
import no.progark19.spacegame.systems.NetworkSystem;
import no.progark19.spacegame.systems.SpawnSystem;
import no.progark19.spacegame.utils.EntityFactory;
import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.ForceOnComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.managers.AudioManager;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.systems.CollisionSystem;
import no.progark19.spacegame.systems.ComponentMappers;
import no.progark19.spacegame.systems.ControlSystem;
import no.progark19.spacegame.systems.ForceApplierSystem;
import no.progark19.spacegame.systems.MovementSystem;
import no.progark19.spacegame.systems.RenderSystem;
import no.progark19.spacegame.systems.SoundSystem;
import no.progark19.spacegame.systems.SpawnSystem;
import no.progark19.spacegame.utils.Paths;
import no.progark19.spacegame.utils.RenderableWorldState;
import no.progark19.spacegame.utils.json.JsonPayload;
import no.progark19.spacegame.utils.json.JsonPayloadTags;
import no.progark19.spacegame.utils.json.WorldStateIndexes;

public class PlayScreen implements Screen, ReceivedDataListener {

    private final SpaceGame game;
    private final Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private Stage uiStage;
    private Camera uiCamera;

    private BitmapFont font;
    private GlyphLayout layout;


    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private AudioManager audioManager;
    private EntityManager entityManager;
    private PooledEngine engine;
    public Sound theme;
    private EntityFactory entityFactory;
    private Texture bg;
    private int bgX = 0;
    private int bgY = 0;

    private Rectangle rectangle;


    //- Private methods ----------------------------------------------------------------------------
    private Slider createEngineSlider(final Entity engineEntity, float posX, float posY, final float minRot, final float maxRot) {
        Slider engineSlider = new Slider(0, 100, 1f, true, game.getSkin());
        engineSlider.setPosition(posX, posY);
        engineSlider.setSize(20, SpaceGame.HEIGHT / 2 - 20);
        engineSlider.setScaleX(3);
        engineSlider.setValue(50);
        engineSlider.addListener(new ChangeListener() {
            float rotDiff = maxRot - minRot;
            HashMap<String, Object> values;
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //spaceShip.changeEngineAngle(engineIndex, ((Slider) actor).getValue());
                //FIXME dette er muligens en litt dårlig løsning på dette [ARH]
                RelativePositionComponent relposcom = ComponentMappers.RELPOS_MAP.get(engineEntity);
                ForceApplierComponent fcom = ComponentMappers.FORCE_MAP.get(engineEntity);


                relposcom.rotation = minRot + rotDiff*((Slider) actor).getValue()/100f;
                fcom.direction = relposcom.rotation + 90;


                JsonPayload jpl = new JsonPayload();
                values = new HashMap<String, Object>();

                values.put(JsonPayloadTags.ENGINE_UPDATE_ENGINEID, EntityManager.getEntityID(engineEntity));
                values.put(JsonPayloadTags.ENGINE_ROTATION_UPDATE_ROTATION, relposcom.rotation);
                values.put(JsonPayloadTags.ENGINE_ROTATION_UPDATE_FORCEDIRECTION, fcom.direction);

                jpl.setTAG(JsonPayloadTags.ENGINE_ROTATION_UPDATE);
                jpl.setValue(values);

                //game.p2pConnector.sendData(jpl);

            }
        });
        engineSlider.addListener(new ClickListener() {
            HashMap<String, Object> values;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                engineEntity.add(new ForceOnComponent());

                JsonPayload jpl = new JsonPayload();
                values = new HashMap<String, Object>();

                values.put(JsonPayloadTags.ENGINE_UPDATE_ENGINEID, EntityManager.getEntityID(engineEntity));
                values.put(JsonPayloadTags.ENGINE_ON_UPDATE_ISON, true);

                jpl.setTAG(JsonPayloadTags.ENGINE_ON_UPDATE);
                jpl.setValue(values);

                //game.p2pConnector.sendData(jpl);

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                engineEntity.remove(ForceOnComponent.class);

                JsonPayload jpl = new JsonPayload();
                HashMap<String, Object> values = new HashMap<String, Object>();

                values.put(JsonPayloadTags.ENGINE_UPDATE_ENGINEID, EntityManager.getEntityID(engineEntity));
                values.put(JsonPayloadTags.ENGINE_ON_UPDATE_ISON, false);

                jpl.setTAG(JsonPayloadTags.ENGINE_ON_UPDATE);
                jpl.setValue(values);

                //game.p2pConnector.sendData(jpl);
            }
        });

        return engineSlider;
    }
    //----------------------------------------------------------------------------------------------
    public PlayScreen(SpaceGame game){
        this.game = game;
        game.camera.setToOrtho(false, SpaceGame.WIDTH, SpaceGame.HEIGHT);
        this.uiCamera = new OrthographicCamera();
        this.uiStage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, uiCamera));
        this.shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawAABBs(true);
        debugRenderer.setDrawVelocities(true);
        engine = new PooledEngine();

        entityManager = new EntityManager();
        entityFactory = new EntityFactory(game, engine);

        //Add engine systems
        engine.addSystem(new ControlSystem(game.camera, entityFactory, engine));
        engine.addSystem(new RenderSystem(game.batch, game.camera, shapeRenderer, game));
        engine.addSystem(new SpawnSystem(engine, game.camera, GameSettings.BOX2D_PHYSICSWORLD, entityFactory));
        engine.addSystem(new MovementSystem(GameSettings.BOX2D_PHYSICSWORLD));
        engine.addSystem(new SoundSystem());
        engine.addSystem(new AnimationSystem(entityFactory, game.batch, engine, GameSettings.BOX2D_PHYSICSWORLD, game.camera, game));
        engine.addSystem(new CollisionSystem(GameSettings.BOX2D_PHYSICSWORLD, game.batch));
        engine.addEntityListener(entityManager);

        if (GameSettings.isPhysicsResponsible) {
            engine.addSystem(new NetworkSystem(GameSettings.WORLDSYNCH_REFRESH_RATE, game.p2pConnector));
            engine.addSystem(new ForceApplierSystem(game));
        }

        //Create entities
        Texture shipTexture = game.assetManager.get(Paths.SPACESHIP_TEXTURE_PATH, Texture.class);
        Texture engineTexture = game.assetManager.get(Paths.ENGINE_TEXTURE_PATH, Texture.class);

        Entity shipEntity = entityFactory.createBaseSpaceShip(
                GameSettings.BOX2D_PHYSICSWORLD, shipTexture
        );
        engine.addEntity(shipEntity);

        Entity engineEntity1 = entityFactory.createShipEngine(
                -23,-50, 315, shipEntity, engineTexture);
        Entity engineEntity2 = entityFactory.createShipEngine(
                -23,50, 225, shipEntity, engineTexture);
        Entity engineEntity3 = entityFactory.createShipEngine(
                23,-50, 45, shipEntity, engineTexture);
        Entity engineEntity4 = entityFactory.createShipEngine(
                23,50, 135, shipEntity, engineTexture);


        engine.addEntity(engineEntity1);
        engine.addEntity(engineEntity2);
        engine.addEntity(engineEntity3);
        engine.addEntity(engineEntity4);

        if(GameSettings.isPhysicsResponsible){
            uiStage.addActor(
                    createEngineSlider(engineEntity1, 10,10,360,270)
            );
            uiStage.addActor(
                    createEngineSlider(engineEntity2, 10,SpaceGame.HEIGHT/2 + 20, 270, 180)
            );
        } else {
            uiStage.addActor(
                    createEngineSlider(engineEntity3, SpaceGame.WIDTH-25,10 ,0, 90)
            );
            uiStage.addActor(
                    createEngineSlider(engineEntity4, SpaceGame.WIDTH-25,SpaceGame.HEIGHT/2 + 20,90,180)
            );
        }

        this.font = new BitmapFont();
        this.layout = new GlyphLayout();


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
        //entityManager.update();

        engine.update(delta);
        //Draw Ui
        //FIXME skal dette være i et ESC system?
        game.batch.setProjectionMatrix(uiCamera.combined);


        font.setColor(Color.WHITE);
        font.getData().setScale(4);
        layout.setText(font, String.valueOf(Gdx.graphics.getFramesPerSecond()));

        font.draw(game.batch, layout,
                SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT/2 - layout.height
        );

        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();

        game.batch.end();

        //Draw physics debug info
        if(GameSettings.BOX2D_DRAWDEBUG){
            debugMatrix = game.camera.combined.cpy().scale(
                    GameSettings.BOX2D_PIXELS_TO_METERS,
                    GameSettings.BOX2D_PIXELS_TO_METERS,
                    0);
            debugRenderer.render(GameSettings.BOX2D_PHYSICSWORLD, debugMatrix);
        }
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
        bg.dispose();
        debugRenderer.dispose();
        shapeRenderer.dispose();
        theme.dispose();
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

    }
}