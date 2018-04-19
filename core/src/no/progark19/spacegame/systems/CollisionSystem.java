package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.AnimationComponent;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.VelocityComponent;

/**
 * Created by anderssalvesen on 17.04.2018.
 */

public class CollisionSystem extends EntitySystem implements ContactListener, EntityListener {

    private ImmutableArray<Entity> bullets;
    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> spaceship;

    private ParticleEffect particleEffect;

    private World world;
    private SpriteBatch batch;


    public CollisionSystem(World world, SpriteBatch batch) {
        this.world = world;
        this.batch = batch;
        world.setContactListener(this);
        //textureAtlas = GameSettings.LARGE_EXPLOSION;

        /*region1 = new TextureAtlas.AtlasRegion("")
        region2 = textureAtlas.findRegion("atlases/explosion2.png");
        region3 = textureAtlas.findRegion("atlases/explosion3.png");
        region4 = textureAtlas.findRegion("atlases/explosion4.png");
        region5 = textureAtlas.findRegion("atlases/explosion5.png");
        region6 = textureAtlas.findRegion("atlases/explosion6.png");
        textureAtlas.addRegion("1", region1);
        textureAtlas.addRegion("2", region2);
        textureAtlas.addRegion("3", region3);
        textureAtlas.addRegion("4", region4);
        textureAtlas.addRegion("5", region5);
        textureAtlas.addRegion("6", region6);
        */
        //particleEffect.loadTextureAtlases(Gdx.files.internal("atlases/explosion.atlas"), Gdx.files.internal("atlases"));
        //particleEffect.loadTextureAtlases(Gdx.files.internal("atlases/explosion.atlas"), textureAtlas);


    }

    public void addedToEngine(Engine engine) {
        bullets = engine.getEntitiesFor(Family.all(
                AnimationComponent.class,
                ElementComponent.class).get());
        asteroids = engine.getEntitiesFor(Family.all(
                BodyComponent.class,
                ElementComponent.class).get());
        spaceship = engine.getEntitiesFor(Family.all(
                BodyComponent.class,
                LeadCameraComponent.class).get());
    }

    public void update(float deltaTime) {

        for (Entity bullet : bullets) {
            SpriteComponent scomBullet = ComponentMappers.SPRITE_MAP.get(bullet);

            for (Entity asteroid : asteroids) {
                SpriteComponent scomAsteroid = ComponentMappers.SPRITE_MAP.get(asteroid);

                if (scomBullet.sprite.getBoundingRectangle().overlaps(scomAsteroid.sprite.getBoundingRectangle())) {
                    //TODO miniexplosion on hit (create new and rescale textureatlas).
                    HealthComponent hcom = ComponentMappers.HEALTH_MAP.get(asteroid);
                    ElementComponent ecomAsteroid = ComponentMappers.ELEMENT_MAP.get(asteroid);
                    ElementComponent ecomBullet = ComponentMappers.ELEMENT_MAP.get(bullet);
                    getEngine().removeEntity(bullet);
                    if (! ecomAsteroid.element.equals(ecomBullet.element)) {
                        hcom.health -= 10;
                    }
                    if (hcom.health == 0) {
                        BodyComponent bcom = ComponentMappers.BOD_MAP.get(asteroid);
                        world.destroyBody(bcom.body);
                        //TODO kick out enity OR REUSE...

                    }
                }
            }
        }
    }


    @Override
    public void beginContact(Contact contact) {
        float x = contact.getWorldManifold().getPoints()[0].x;
        float y = contact.getWorldManifold().getPoints()[0].y;
        short a = contact.getFixtureA().getFilterData().categoryBits;
        short b = contact.getFixtureB().getFilterData().categoryBits;
        System.out.println(x + " " + y);
        System.out.println(a);
        System.out.println(b);
        if (a == GameSettings.FIRE_ASTEROID_TAG && b == GameSettings.FIRE_ASTEROID_TAG) {
            System.out.println("Fire collided");
        }
        if (a == GameSettings.ICE_ASTEROID_TAG && b == GameSettings.ICE_ASTEROID_TAG) {
            System.out.println("Ice collided");
        }
        if ((a == GameSettings.FIRE_ASTEROID_TAG && b == GameSettings.ICE_ASTEROID_TAG) || (a == GameSettings.ICE_ASTEROID_TAG && b == GameSettings.FIRE_ASTEROID_TAG)) {
            System.out.println("Fire and ice collided");
        }
        if ((a == GameSettings.SPACESHIP_TAG && (b == GameSettings.ICE_ASTEROID_TAG || b == GameSettings.FIRE_ASTEROID_TAG))
                || (b == GameSettings.SPACESHIP_TAG && (a == GameSettings.ICE_ASTEROID_TAG || a == GameSettings.FIRE_ASTEROID_TAG))) {
            System.out.println("Spaceship ");
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void entityAdded(Entity entity) {

    }

    @Override
    public void entityRemoved(Entity entity) {

    }


    public void dispose() {
        batch.dispose();
    }
}
