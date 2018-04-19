package no.progark19.spacegame.screens;

import com.badlogic.ashley.core.ComponentMapper;
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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.time.Clock;
import java.util.Date;
import java.util.HashMap;

import no.progark19.spacegame.interfaces.ReceivedDataListener;
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
import no.progark19.spacegame.utils.json.JsonPayload;
import no.progark19.spacegame.utils.json.JsonPayloadTags;

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
    //private Skin skin2;
    private Skin skin;

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
            //float lastSentValueDiff = ;
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

                game.p2pConnector.sendData(jpl);

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

                game.p2pConnector.sendData(jpl);

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

                game.p2pConnector.sendData(jpl);
            }
        });

        return engineSlider;
    }
    //----------------------------------------------------------------------------------------------
    public PlayScreen(SpaceGame game){
        this.game = game;
        game.camera.setToOrtho(false, SpaceGame.WIDTH*1.5f, SpaceGame.HEIGHT*1.5f);
        this.uiCamera = new OrthographicCamera();
        this.uiStage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, uiCamera));
        this.shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawAABBs(true);
        debugRenderer.setDrawVelocities(true);
        engine = new PooledEngine();

        entityManager = new EntityManager();
        entityFactory = new EntityFactory(engine);

        //Add engine systems
        //engine.addSystem(new ControlSystem());
        engine.addSystem(new RenderSystem(game.batch, game.camera));
        //engine.addSystem(new SpawnSystem(engine, game.camera, GameSettings.BOX2D_PHYSICSWORLD, entityFactory));
        engine.addSystem(new MovementSystem(GameSettings.ESC_MOVEMENT_INTERVAL));
        //engine.addSystem(new CollisionSystem());
        //engine.addSystem(new SoundSystem());
        engine.addSystem(new ForceApplierSystem());
        engine.addEntityListener(entityManager);

        //Create entities
        Texture shipTexture = new Texture(GameSettings.SPACESHIP_TEXTURE_PATH);
        Texture engineTexture = new Texture(GameSettings.ENGINE_TEXTURE_PATH);

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

        if(GameSettings.isLeftPlayer){
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

        //this.skin2 = new Skin(Gdx.files.internal("ui/sgx/sgxui.json"));
        //this.skin2.addRegions(new TextureAtlas("ui/sgx/sgxui.atlas"));

        this.skin = new Skin();
        this.skin.addRegions(game.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        game.p2pConnector.addReceivedDataListener(this);
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

        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();

        //TODO This was supposed to print the FPS, but doesnt!
        font.setColor(Color.WHITE);
        font.getData().setScale(4);
        layout.setText(font, String.valueOf(Gdx.graphics.getFramesPerSecond()));

        font.draw(game.batch, layout,
                SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT/2 - layout.height
        );

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
    /*
public void changed(ChangeEvent event, Actor actor) {
                //spaceShip.changeEngineAngle(engineIndex, ((Slider) actor).getValue());
                //FIXME dette er muligens en litt dårlig løsning på dette [ARH]
                RelativePositionComponent relposcom = ComponentMappers.RELPOS_MAP.get(engineEntity);
                ForceApplierComponent fcom = ComponentMappers.FORCE_MAP.get(engineEntity);


                relposcom.rotation = minRot + rotDiff*((Slider) actor).getValue()/100f;
                fcom.direction = relposcom.rotation + 90;


                JsonPayload jpl = new JsonPayload();
                HashMap<String, Object> values = new HashMap<String, Object>();

                values.put(JsonPayloadTags.ENGINE_UPDATE_ENGINEID, EntityManager.getEntityID(engineEntity));
                values.put(JsonPayloadTags.ENGINE_ROTATION_UPDATE_ROTATION, relposcom.rotation);
                values.put(JsonPayloadTags.ENGINE_ROTATION_UPDATE_FORCEDIRECTION, fcom.direction);

                jpl.setTAG(JsonPayloadTags.ENGINE_ROTATION_UPDATE);
                jpl.setValue(values);

            }
        });

    */
    @Override
    public void onReceive(JsonPayload data) {
        int TAG = data.getTAG();
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
            default:
                System.out.println("NOT LEGAL JSON TAG");
        }
    }

    @Override
    public void onReceive(String data) {

    }
}