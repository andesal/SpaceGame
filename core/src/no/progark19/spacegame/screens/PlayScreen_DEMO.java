package no.progark19.spacegame.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.gameObjects.SpaceShip;
import no.progark19.spacegame.gameObjects.SpaceShipEngine;
import no.progark19.spacegame.gameObjects.Square;
import no.progark19.spacegame.managers.EntityManager;

/**
 * Lager denne klassen kun for å teste dritt, legger det vi trenger til i Playscreen når det funker
 */

public class PlayScreen_DEMO implements Screen {
    private final SpaceGame game;
    private Stage stage;
    private ShapeRenderer shapeRenderer;

    public EntityManager entityManager;
    private Engine engine;

    // Textures
    private Texture background;

    // Sprites
    //private Sprite sprite_Spaceship;
    private SpaceShip spaceShip;

    // Box2d values and objects
    private World world;                                // Simulates physics on bodies
    private Body body_Spaceship;                        // Spaceship world-object
    private Matrix4 debugMatrix;                        // Contains draw-values, like velocity etc.
    private Box2DDebugRenderer debugRenderer;           // Used to draw the simulation

    // UI elements
    private Camera uiCamera;    // A static camera that displays the UI
    private Stage uiStage;      // Contains all acting ui-elements

    private Body createDynamicBody(Sprite sprite, World world,
                                   PolygonShape shape, float density, float restitution){
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/GameSettings.BOX2D_PIXELS_TO_METERS,
                             (sprite.getY() + sprite.getHeight()/2)/GameSettings.BOX2D_PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        if (shape == null){
            shape = new PolygonShape();
            shape.setAsBox(sprite.getWidth()/2  / GameSettings.BOX2D_PIXELS_TO_METERS,
                           sprite.getHeight()/2 / GameSettings.BOX2D_PIXELS_TO_METERS);
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;

        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    private void createEngineSlider(final int engineIndex, float posX, float posY){
        Slider engineSlider = new Slider(0,100,0.1f, true, game.getSkin());
        engineSlider.setPosition(posX,posY);
        engineSlider.setSize(20, SpaceGame.HEIGHT/2 - 20);
        engineSlider.setScaleX(3);
        engineSlider.setValue(50);
        engineSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                spaceShip.changeEngineAngle(engineIndex, ((Slider) actor).getValue());
            }
        });
        engineSlider.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchdown");
                spaceShip.setEngineOn(engineIndex, true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                spaceShip.setEngineOn(engineIndex, false);
            }
        });

        uiStage.addActor(engineSlider);
    }
    /*TODO {1} -----------------------------------------------------------------------*/

    public PlayScreen_DEMO(SpaceGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();

        /*TODO {1} NOT IN PlayScreen -----------------------------------------------------*/
        uiCamera = new OrthographicCamera();
        uiStage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, uiCamera));

        // Set up textures & sprites etc -----------------------------------------------------------
        //background = new Texture("img/paralax_space2.png");
        Sprite sprite_Spaceship = new Sprite(new Texture("img/spaceship.png"));
        Sprite engineSprite = new Sprite(new Texture("img/spaceship_engine.png"));
        engineSprite.setOrigin(GameSettings.ENGINE_ORIGIN.x, GameSettings.ENGINE_ORIGIN.y);

        // Set up box2d ----------------------------------------------------------------------------
        world = new World(new Vector2(0,0), true);
        debugRenderer = new Box2DDebugRenderer();

        // Create Spaceship ------------------------------------------------------------------------
        spaceShip = new SpaceShip(sprite_Spaceship);
        // Place Spaceship
        spaceShip.setPosition(SpaceGame.WIDTH/2 - sprite_Spaceship.getWidth()/2,
                SpaceGame.HEIGHT/2 - sprite_Spaceship.getHeight()/2);
        // Create body for spaceship
        body_Spaceship = createDynamicBody(sprite_Spaceship, world, null, 0.5f, 0.5f);
        //body_Spaceship.
        spaceShip.setBody_baseShip(body_Spaceship);
        // Add engines
        //TODO do this dynamically based on GameSettings
        //Slider 1 [Bottom left]
        spaceShip.addEngine(new Vector2(-23,-50), engineSprite, 360, 270);  //Bottom left
        createEngineSlider(0, 10, 10);
        //Slider 2 [Top left]
        spaceShip.addEngine(new Vector2(-23,50), engineSprite, 270, 180);  //Top left
        createEngineSlider(1, 10,SpaceGame.HEIGHT/2 + 20);
        //Slider 3 [Bottom Right]
        spaceShip.addEngine(new Vector2(23, -50), engineSprite, 0, 90 );  //Bottom right
        createEngineSlider(2,SpaceGame.WIDTH-25,10 );
        //Slider 4 [Top Right]
        spaceShip.addEngine(new Vector2(23, 50),  engineSprite, 90, 180);   //Top right
        createEngineSlider(3,SpaceGame.WIDTH-25,SpaceGame.HEIGHT/2 + 20 );

        // Enable stage input ----------------------------------------------------------------------
        debugRenderer.setDrawVelocities(GameSettings.BOX2D_DRAWDEBUG);
        debugRenderer.setDrawAABBs(GameSettings.BOX2D_DRAWDEBUG);
        /*TODO {1} -----------------------------------------------------------------------*/



        // --------------ANDERS----------------
        engine = new Engine();
        entityManager = new EntityManager(engine, game.batch);
        // --------------ANDERS----------------
    }

    @Override
    public void show() {
        //TODO is there anything we should do here?
        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    public void render(float delta) {
        /*TODO {1} -----------------------------------------------------------------------*/
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Step world simulation
        world.step(1f/60f, 6,2);

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        entityManager.update();

        //game.batch.draw(background, 0,0);

        spaceShip.setRotation((float) Math.toDegrees(body_Spaceship.getAngle()));
        spaceShip.setPosition(
                (body_Spaceship.getPosition().x * GameSettings.BOX2D_PIXELS_TO_METERS) - spaceShip.getWidth() / 2,
                (body_Spaceship.getPosition().y * GameSettings.BOX2D_PIXELS_TO_METERS) - spaceShip.getHeight() / 2
        );
        spaceShip.draw(game.batch);

        //Make camera follow ship
        if (GameSettings.CAMERA_FOLLOW_ROTATION){
            game.camera.up.set(0,1,0);
            game.camera.direction.set(0,0,-1);
            game.camera.rotate(-spaceShip.getRotation());
        }
        if (GameSettings.CAMERA_FOLLOW_POSITION){
            game.camera.position.set(spaceShip.getOriginWorldpoint().x, spaceShip.getOriginWorldpoint().y, 0);
        }
        game.camera.update();

        //Draw Ui
        game.batch.setProjectionMatrix(uiCamera.combined);

        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();


        game.batch.end();

        //Draw physics debug info
        if(GameSettings.BOX2D_DRAWDEBUG){
            debugMatrix = game.camera.combined.cpy().scale(
                    GameSettings.BOX2D_PIXELS_TO_METERS,
                    GameSettings.BOX2D_PIXELS_TO_METERS,
                    0);
            debugRenderer.render(world, debugMatrix);
        }
        /*TODO {1} NOT IN PlayScreen -----------------------------------------------------*/
        System.out.println("X: " + spaceShip.getX() + ", Y: " + spaceShip.getY());
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
}