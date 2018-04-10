package no.progark19.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.progark19.spacegame.SpaceGame;

/**
 * Lager denne klassen kun for å teste dritt, legger det vi trenger til i Playscreen når det funker
 */

public class PlayScreen_DEMO implements Screen {

    private final SpaceGame game;
    private Stage stage;
    private ShapeRenderer shapeRenderer;

    /*TODO{1} NOT IN PlayScreen -----------------------------------------------------*/
    //DEBUGSTUFF -TODO REMOVE
    private Texture controllDebugOverlay;

    // Viewstuff
    private Viewport viewport;

    // Textures
    private Texture background;

    // Sprites
    private Sprite sprite_Spaceship;
    private Sprite sprite_engine;

    // Box2d values and objects
    private static final float PIXELS_TO_METERS = 100f;     //Used to scale box2d drawings
    private World world;                // Simulates physics on bodies
    private Body body_Spaceship;        // Spaceship world-object
    private Body body_engine1;
    private Body body_engine2;
    private Body body_engine3;
    private Body body_engine4;
    private Matrix4 box2dDebugMatrix;   // Contains draw-values, like velocity etc.
    private Box2DDebugRenderer debugRenderer; // Used to draw the simulation

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
    /*TODO {1} -----------------------------------------------------------------------*/
    public PlayScreen_DEMO(SpaceGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(SpaceGame.WIDTH, SpaceGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();

        /*TODO {1} NOT IN PlayScreen -----------------------------------------------------*/
        // Set up textures & sprites etc -----------------------------------------------------------
        background = new Texture("img/paralax_space2.png");
        sprite_Spaceship = new Sprite(new Texture("img/spaceship.png"));
        sprite_Spaceship.setPosition(SpaceGame.WIDTH/2 - sprite_Spaceship.getWidth()/2,
                                     SpaceGame.HEIGHT/2 - sprite_Spaceship.getHeight()/2);
        sprite_engine = new Sprite(new Texture("img/spaceship_engine.png"));

        // Set up box2d ----------------------------------------------------------------------------
        world = new World(new Vector2(0,0), true);
        // Create body for spaceship
        body_Spaceship = createDynamicBody(sprite_Spaceship, world, null, 0.5f, 0.5f);

        sprite_engine.setPosition(0,0);
        body_engine1 = createDynamicBody(sprite_engine, world, null, 1.0f,0.1f);

        sprite_engine.setPosition(0,70);
        body_engine2 = createDynamicBody(sprite_engine, world, null, 1f,0.1f);

        sprite_engine.setPosition(0,140);
        body_engine3 = createDynamicBody(sprite_engine, world, null, 1f,0.1f);

        sprite_engine.setPosition(0,210);
        body_engine4 = createDynamicBody(sprite_engine, world, null, 1f,0.1f);


        body_engine1.setLinearVelocity(1,1);
        body_engine2.setLinearVelocity(1,1);
        body_engine3.setLinearVelocity(1,1);
        body_engine4.setLinearVelocity(1,1);

        debugRenderer = new Box2DDebugRenderer();
        // Set up camera ---------------------------------------------------------------------------

        //body_Spaceship.setAngularVelocity(0.1f);
        //body_Spaceship.setLinearVelocity(0,0.5f);
       /*TODO {1} -----------------------------------------------------------------------*/
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

        sprite_Spaceship.setPosition(
                (body_Spaceship.getPosition().x * PIXELS_TO_METERS) - sprite_Spaceship.getWidth() / 2,
                (body_Spaceship.getPosition().y * PIXELS_TO_METERS) - sprite_Spaceship.getHeight() / 2
        );
        sprite_Spaceship.setRotation((float) Math.toDegrees(body_Spaceship.getAngle()));

        //Make camera follow ship
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0,0);

        sprite_Spaceship.draw(game.batch);

        //draw engines
        sprite_engine.setPosition(
                (body_engine1.getPosition().x * PIXELS_TO_METERS) - sprite_engine.getWidth() / 2,
                (body_engine1.getPosition().y * PIXELS_TO_METERS) - sprite_engine.getHeight() / 2
        );
        sprite_engine.setRotation((float) Math.toDegrees(body_engine1.getAngle()));
        sprite_engine.draw(game.batch);

        sprite_engine.setPosition(
                (body_engine2.getPosition().x * PIXELS_TO_METERS) - sprite_engine.getWidth() / 2,
                (body_engine2.getPosition().y * PIXELS_TO_METERS) - sprite_engine.getHeight() / 2
        );
        sprite_engine.setRotation((float) Math.toDegrees(body_engine2.getAngle()));
        sprite_engine.draw(game.batch);


        sprite_engine.setPosition(
                (body_engine3.getPosition().x * PIXELS_TO_METERS) - sprite_engine.getWidth() / 2,
                (body_engine3.getPosition().y * PIXELS_TO_METERS) - sprite_engine.getHeight() / 2
        );
        sprite_engine.setRotation((float) Math.toDegrees(body_engine3.getAngle()));
        sprite_engine.draw(game.batch);

        sprite_engine.setPosition(
                (body_engine4.getPosition().x * PIXELS_TO_METERS) - sprite_engine.getWidth() / 2,
                (body_engine4.getPosition().y * PIXELS_TO_METERS) - sprite_engine.getHeight() / 2
        );
        sprite_engine.setRotation((float) Math.toDegrees(body_engine4.getAngle()));
        sprite_engine.draw(game.batch);

        game.batch.end();

        //TODO Make camera rotation follow box [ARH]
        game.camera.up.set(0,1,0);
        game.camera.direction.set(0,0,-1);
        game.camera.rotate(-sprite_Spaceship.getRotation());
        game.camera.position.set(sprite_Spaceship.getX() + sprite_Spaceship.getWidth()/2, sprite_Spaceship.getY() + sprite_Spaceship.getHeight()/2, 0);

        game.camera.update();

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