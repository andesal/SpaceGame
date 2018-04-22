package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import no.progark19.spacegame.utils.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.DamagedComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.RewardComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.SweepComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.ComponentMappers;
import no.progark19.spacegame.utils.EntityFactory;
import no.progark19.spacegame.utils.Paths;

/**
 * Created by anderssalvesen on 17.04.2018.
 */

public class CollisionSystem extends EntitySystem implements ContactListener {

    private ImmutableArray<Entity> bullets;
    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> spaceship;
    private ImmutableArray<Entity> powerups;


    private SpaceGame game;


    public CollisionSystem(SpaceGame game) {
        this.game = game;
        GameSettings.BOX2D_PHYSICSWORLD.setContactListener(this);

    }

    public void addedToEngine(Engine engine) {
        bullets = engine.getEntitiesFor(Family.all(
                ElementComponent.class, SpriteComponent.class, RenderableComponent.class)
                .exclude(BodyComponent.class).get());
        asteroids = engine.getEntitiesFor(Family.all(
                BodyComponent.class,
                ElementComponent.class).get());
        spaceship = engine.getEntitiesFor(Family.all(
                BodyComponent.class,
                LeadCameraComponent.class).get());
        powerups = engine.getEntitiesFor(Family.all(PowerupComponent.class).get());
    }

    public void update(float deltaTime) {
        for (Entity ship : spaceship) {
            for (Entity powerup : powerups) {
                SpriteComponent scomShip = ComponentMappers.SPRITE_MAP.get(ship);
                SpriteComponent scomPowerUp = ComponentMappers.SPRITE_MAP.get(powerup);
                PowerupComponent powCom = ComponentMappers.POWER_MAP.get(powerup);
                if (scomPowerUp.sprite.getBoundingRectangle().overlaps(scomShip.sprite.getBoundingRectangle())) {
                    getEngine().removeEntity(powerup);
                    if (powCom.powerup == EntityFactory.POWERUPS.HEALTH) {
                        ship.add(new RewardComponent(10, "health"));
                    } else {
                        ship.add(new RewardComponent(10, "fuel"));
                    }
                }
            }
        }

        for (Entity asteroid : asteroids) {
            SpriteComponent scomAsteroid = ComponentMappers.SPRITE_MAP.get(asteroid);
            for (Entity bullet : bullets) {
                SpriteComponent scomBullet = ComponentMappers.SPRITE_MAP.get(bullet);
                ElementComponent ecomBullet = ComponentMappers.ELEMENT_MAP.get(bullet);
                ElementComponent ecomAsteroid = ComponentMappers.ELEMENT_MAP.get(asteroid);
                if (scomBullet.sprite.getBoundingRectangle().overlaps(scomAsteroid.sprite.getBoundingRectangle()) ) {
                    if (ecomBullet.element != ecomAsteroid.element) {
                        asteroid.add(new DamagedComponent(10));
                    }
                    bullet.add(new SweepComponent());
                }
            }
        }
    }


    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        short a = fa.getFilterData().categoryBits;
        short b = fb.getFilterData().categoryBits;
        Entity entityA = (Entity) fa.getBody().getUserData();
        Entity entityB = (Entity) fb.getBody().getUserData();

        if ((a == GameSettings.SPACESHIP_TAG && (b == GameSettings.ICE_ASTEROID_TAG || b == GameSettings.FIRE_ASTEROID_TAG)) || (b == GameSettings.SPACESHIP_TAG && (a == GameSettings.ICE_ASTEROID_TAG || a == GameSettings.FIRE_ASTEROID_TAG))) {
            Sound sound = game.assetManager.get(Paths.SOUND_COLLISION_SPACESHIP, Sound.class);
            sound.play(0.1f * GameSettings.EFFECTS_VOLUME);
            Entity ship = a == GameSettings.SPACESHIP_TAG ? entityA : entityB;
            ship.add(new DamagedComponent(10));
            return;
        }

        SpriteComponent scomA = ComponentMappers.SPRITE_MAP.get(entityA);
        SpriteComponent scomB = ComponentMappers.SPRITE_MAP.get(entityB);

        float xA = scomA.sprite.getX(); float xB = scomB.sprite.getX();
        float yA = scomA.sprite.getY(); float yB = scomB.sprite.getY();

        //Collision happens reasonably close, trigger explosion
        if (GameSettings.screenBounds.contains(xA, yA) || GameSettings.screenBounds.contains(xB, yB)) {
            EntityManager.flaggedForRemoval.add(entityA);
            EntityManager.flaggedForRemoval.add(entityB);
            Sound sound = game.assetManager.get(Paths.SOUND_ASTEROID_EXPLOSION, Sound.class);
            sound.play(0.1f * GameSettings.EFFECTS_VOLUME);
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



}
