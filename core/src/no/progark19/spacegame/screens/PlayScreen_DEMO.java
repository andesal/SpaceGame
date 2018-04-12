package no.progark19.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.gameObjects.SpaceShip;
import no.progark19.spacegame.gameObjects.SpaceShipEngine;

/**
 * Lager denne klassen kun for å teste dritt, legger det vi trenger til i Playscreen når det funker
 */

public class PlayScreen_DEMO implements Screen {
    private final SpaceGame game;
    private Stage stage;
    private ShapeRenderer shapeRenderer;

    /*TODO{1} NOT IN PlayScreen -----------------------------------------------------*/
    //DEBUGSTUFF -TODO REMOVE
    private Sprite controllDebugOverlay;
    private boolean rotateCamera = true;
    private boolean drawPhysicsDebug = true;

    // Viewstuff
    private Viewport viewport;

    // Textures
    private Texture background;


    // Sprites
    private Sprite sprite_Spaceship;
    //private Sprite sprite_engine;
    private SpaceShip spaceShip;

    // Box2d values and objects
    public static final float PIXELS_TO_METERS = 100f;     //Used to scale box2d drawings
    private World world;                // Simulates physics on bodies
    private Body body_Spaceship;        // Spaceship world-object
    private Matrix4 debugMatrix;   // Contains draw-values, like velocity etc.
    private Box2DDebugRenderer debugRenderer; // Used to draw the simulation

    // UI elements
    private Camera uiCamera;
    private Stage uiStage;
    //private Slider engineSlider1;
    //private Slider engineSlider2;
    //private Slider engineSlider3;
    //private Slider engineSlider4;

    private Body createDynamicBody(Sprite sprite, World world,
                                   PolygonShape shape, float density, float restitution){
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/PIXELS_TO_METERS,
                             (sprite.getY() + sprite.getHeight()/2)/PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        if (shape == null){
            shape = new PolygonShape();
            shape.setAsBox(sprite.getWidth()/2  / PIXELS_TO_METERS,
                           sprite.getHeight()/2 / PIXELS_TO_METERS);
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
        //engineSlider1.scaleBy(1,2);
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
        background = new Texture("img/paralax_space2.png");
        sprite_Spaceship = new Sprite(new Texture("img/spaceship.png"));
        Sprite engineSprite = new Sprite(new Texture("img/spaceship_engine.png"));
        engineSprite.setOrigin(SpaceShipEngine.ENGINE_ORIGIN.x, SpaceShipEngine.ENGINE_ORIGIN.y);

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
        spaceShip.addEngine(new Vector2(-23,-50), engineSprite, 360, 270);  //Bottom left
        spaceShip.addEngine(new Vector2(-23,50), engineSprite, 270, 180);  //Top left
        spaceShip.addEngine(new Vector2(23, -50), engineSprite, 0, 90 );  //Bottom right
        spaceShip.addEngine(new Vector2(23, 50),  engineSprite, 90, 180);   //Top right


        // Set up camera ---------------------------------------------------------------------------

        //Setup UI-sliders
        //Slider 1 [Bottom left]
        createEngineSlider(0, 10, 10);
        //Slider 2 [Top left]
        createEngineSlider(1, 10,SpaceGame.HEIGHT/2 + 20);
        //Slider 3 [Bottom Right]
        createEngineSlider(2,SpaceGame.WIDTH-25,10 );
        //Slider 4 [Top Right]
        createEngineSlider(3,SpaceGame.WIDTH-25,SpaceGame.HEIGHT/2 + 20 );
        Gdx.input.setInputProcessor(uiStage);

        /*TODO {1} -----------------------------------------------------------------------*/
        //FIXME REMOVE
        controllDebugOverlay = new Sprite(new Texture("img/playscreenTestDebugOverlay.png"));
        debugRenderer.setDrawVelocities(true);

        //body_Spaceship.setAngularVelocity(0.5f);
        //body_Spaceship.setLinearVelocity(0,0.5f);


    }

    @Override
    public void show() {
        //viewport = new StretchViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, game.camera);
        //viewport.apply();
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
        game.batch.draw(background, 0,0);

        spaceShip.setRotation((float) Math.toDegrees(body_Spaceship.getAngle()));
        spaceShip.setPosition(
                (body_Spaceship.getPosition().x * PIXELS_TO_METERS) - sprite_Spaceship.getWidth() / 2,
                (body_Spaceship.getPosition().y * PIXELS_TO_METERS) - sprite_Spaceship.getHeight() / 2
        );

        //Make camera follow ship


        spaceShip.draw(game.batch);

        if (rotateCamera){
            game.camera.up.set(0,1,0);
            game.camera.direction.set(0,0,-1);
            game.camera.rotate(-sprite_Spaceship.getRotation());
        }
        game.camera.position.set(sprite_Spaceship.getX() + sprite_Spaceship.getWidth()/2, sprite_Spaceship.getY() + sprite_Spaceship.getHeight()/2, 0);
        game.camera.update();

        // Draw physics debug overlay


        //Draw Ui
        game.batch.setProjectionMatrix(uiCamera.combined);
        //FIXME: Remove
        //game.batch.draw(controllDebugOverlay, -50,-50);

        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();

        game.batch.end();

        //Draw physics debug info
        if(drawPhysicsDebug){
            debugMatrix = game.camera.combined.cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);
            debugRenderer.render(world, debugMatrix);
        }
        /*TODO {1} NOT IN PlayScreen -----------------------------------------------------*/
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