package no.progark19.spacegame.screens;

import com.badlogic.gdx.Screen;
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
    // Viewstuff
    private Viewport viewport;

    // Textures
    private Texture background;

    // Sprites
    private Sprite sprite_Spaceship;

    // Box2d values and objects
    private static final float PIXELS_TO_METERS = 100f;     //Used to scale box2d drawings
    private World world;                // Simulates physics on bodies
    private Body body_Spaceship;        // Spaceship world-object
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

        // Set up box2d ----------------------------------------------------------------------------
        world = new World(new Vector2(0,0), true);
        // Create body for spaceship
        body_Spaceship = createDynamicBody(sprite_Spaceship, world, null, 0.1f, 0.5f);

        debugRenderer = new Box2DDebugRenderer();
        // Set up camera ---------------------------------------------------------------------------

        body_Spaceship.setLinearVelocity(0.2f,0.2f);
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
        //Step world simulation
        world.step(1f/60f, 6,2);

        sprite_Spaceship.setPosition(
                (body_Spaceship.getPosition().x * PIXELS_TO_METERS) - sprite_Spaceship.getWidth() / 2,
                (body_Spaceship.getPosition().y * PIXELS_TO_METERS) - sprite_Spaceship.getHeight() / 2
        );


        game.batch.begin();
        game.batch.draw(background, 0,0);

        sprite_Spaceship.draw(game.batch);
        game.batch.end();

        //Make camera follow ship
        game.camera.position.set(sprite_Spaceship.getX(), sprite_Spaceship.getY(), 0);
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