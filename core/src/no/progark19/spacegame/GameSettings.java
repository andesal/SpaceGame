package no.progark19.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.lang.reflect.Array;
import java.util.Random;

/**
 * Created by Anders on 12.04.2018.
 */

public class GameSettings {
    public static boolean isPhysicsResponsible = false;

    public static final boolean CAMERA_FOLLOW_POSITION = true;
    public static final boolean CAMERA_FOLLOW_ROTATION = true;
    public static final boolean BOX2D_DRAWDEBUG = true;
    public static final float BOX2D_PIXELS_TO_METERS = 100f;  // Used to scale box2d drawings
    public final static World BOX2D_PHYSICSWORLD = new World(new Vector2(0,0), true);

    public static final boolean SPACESHIP_STABILIZE_ROTATION = true;
    public static final float SPACESHIP_STABILIZATION_SCALAR = 0.995f;
    public static final float SPACESHIP_DENSITY = 0.5f;
    public static final float SPACESHIP_RESTITUTION = 0.5f;
    public static final boolean SPACESHIP_ENABLE_ROTATION = true;
    public final static String SPACESHIP_TEXTURE_PATH = "img/spaceship.png";
    public final static Vector2 ENGINE_ORIGIN = new Vector2(9,25);
    public final static float ENGINE_MAX_FORCE = 0.1f;
    public final static String ENGINE_TEXTURE_PATH = "img/spaceship_engine.png";
    public final static String BACKGROUND_PATH = "img/background.png";

    public final static String DEBUG_FORCEARROW_TEXTURE_PATH = "img/debug_forcearrow.png";
    public final static Vector2 DEBUG_FORCEARROW_ORIGIN = new Vector2(56.5f, 51.5f);

    public final static String ASTEROID_FIRE_TEXTURE_PATH = "img/asteroid_fire.png";
    public final static String ASTEROID_ICE_TEXTURE_PATH = "img/asteroid_ice.png";

    public final static float PROJECTILE_SCALE = 0.05f;
    public final static String FIRE_PROJECTILE_REGION = "atlasLib/f.png";
    public final static String ICE_PROJECTILE_REGION = "img/ice_region.png";

    //Tags for collision filtering
    public final static short SPACESHIP_TAG = 0x0002;
    public final static short FIRE_ASTEROID_TAG = 0x0004;
    public final static short ICE_ASTEROID_TAG = 0x0008;

    //ANIMATION ATLASES:
    public final static String FIRE_EXPLOSION = "atlasLib/fire_explosion.atlas";
    public final static String ICE_EXPLOSION = "atlasLib/ice.atlas";

    public final static int MAX_ASTEROIDS = 30;


    //public static String gameFrameRate
    public final static int WORLDSYNC_MAXSTATES = 5;
    public static final float WORLDSYNCH_REFRESH_RATE = 1/3f;


    private static Random mainRandom;

    public static void setRandomSeed(long seed){
        System.out.println("GotSeed:" + seed);
        mainRandom = new Random(seed);

    }

    public static Random getMainRandom(){
        if (mainRandom == null) {
            throw new IllegalStateException("mainRandom not yet set!");
        } else {
            return mainRandom;
        }
    }

    //FIXME move to somewhere else
    public static Body createDynamicBody(Sprite sprite, World world,
                                         Shape shape, float density, float restitution, short filterID){
        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2)/GameSettings.BOX2D_PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2)/GameSettings.BOX2D_PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        if (shape == null){
            shape = new PolygonShape();
            ((PolygonShape)shape).setAsBox(sprite.getWidth()/2  / GameSettings.BOX2D_PIXELS_TO_METERS,
                    sprite.getHeight()/2 / GameSettings.BOX2D_PIXELS_TO_METERS);
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.filter.categoryBits = filterID;
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }


    public static Body generatePolygon(float x, float y, World world, Texture texture, PolygonSprite polygonSprite, short filterID) {
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / BOX2D_PIXELS_TO_METERS, y / BOX2D_PIXELS_TO_METERS);

        body = world.createBody(bodyDef);
        //System.out.println(body.getPosition().x);
        //System.out.println(body.getPosition().y);

        PolygonShape shape = new PolygonShape();

        float vertexArray[] = {
                0.0f, 0.3f,
                0.1f, 0.0f,
                0.4f, 0.0f,
                0.5f, 0.3f,
                0.25f, 0.5f
        };

        shape.set(vertexArray);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.7f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = filterID;

        body.createFixture(fixtureDef);
        shape.dispose();

        short triangles[] = new EarClippingTriangulator().computeTriangles(vertexArray).toArray();
        // use your texture region
        //Texture texture = new Texture("badlogic.jpg");
        TextureRegion textureRegion = new TextureRegion(texture);
        PolygonRegion polygonRegion = new PolygonRegion(textureRegion, vertexArray, triangles);

        polygonSprite = new PolygonSprite(polygonRegion);
        polygonSprite.setOrigin(0, 0);

        return body;
    }
}
