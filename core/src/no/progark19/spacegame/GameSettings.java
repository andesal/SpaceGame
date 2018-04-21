package no.progark19.spacegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Locale;
import java.util.Random;


public class GameSettings {
    public static boolean isLeftPlayer = false;

    public static final float ESC_MOVEMENT_INTERVAL = 1/80f;

    public static final boolean CAMERA_FOLLOW_POSITION = true;
    public static final boolean CAMERA_FOLLOW_ROTATION = true;
    public static final boolean BOX2D_DRAWDEBUG = true;
    public static final float BOX2D_PIXELS_TO_METERS = 100f;  // Used to scale box2d drawings
    public final static World BOX2D_PHYSICSWORLD = new World(new Vector2(0,0), true);

    public static final boolean SPACESHIP_STABILIZE_ROTATION = true;
    public static final float SPACESHIP_STABILIZATION_SCALAR = 0.995f;
    public static final float SPACESHIP_DENSITY = 0.5f;
    public static final float SPACESHIP_RESTITUTION = 0.5f;
    public static final boolean SPACESHIP_ENABLE_ROTATION = false;
    public final static Vector2 ENGINE_ORIGIN = new Vector2(9,25);
    public final static float ENGINE_MAX_FORCE = 0.1f;

    public final static Vector2 DEBUG_FORCEARROW_ORIGIN = new Vector2(56.5f, 51.5f);
    public final static float PROJECTILE_SCALE = 0.05f;


    //Tags for collision filtering
    public final static short SPACESHIP_TAG = 0x0002;
    public final static short FIRE_ASTEROID_TAG = 0x0004;
    public final static short ICE_ASTEROID_TAG = 0x0008;

    // Gameplay settings
    public final static int MAX_ASTEROIDS = 30;
    public static Rectangle screenBounds = new Rectangle(-240, 720, 960, 960); //starting bounding box

    public final static int MAX_HEALTH_SPACESHIP = 100;
    public final static int MAX_HEALTH_ASTEROIDS = 50;

    public final static int START_HEALTH = 60;
    public final static float START_FUEL = 50f;
    public final static float MAX_FUEL = 100f;

    public static int GAME_STATE = 1; //Default Playstate


    //public static String gameFrameRate

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


    public static Animation createAnimation(TextureAtlas atlas, float frameDuration) {
        com.badlogic.gdx.utils.Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        TextureAtlas.AtlasRegion[] frames = new TextureAtlas.AtlasRegion[regions.size];
        for(int i = 0; i < frames.length; i++) {
            frames[i] = atlas.findRegion("explosion" + String.format(Locale.getDefault(), "%04d", Integer.parseInt(String.valueOf(i))));
        }
        return new Animation<TextureRegion>(frameDuration, frames);
    }




}
