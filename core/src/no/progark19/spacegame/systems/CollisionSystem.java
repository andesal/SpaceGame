package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.GravityComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.utils.EntityFactory;

/**
 * Created by anderssalvesen on 10.04.2018.
 */

public class CollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> spaceship;
    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> projectiles;
    private ImmutableArray<Entity> obstacles;
    private Rectangle boundsSpaceship;
    private Sprite spaceshipSprite;
    private EntityFactory entityFactory;

    public CollisionSystem(Sprite sprite, EntityFactory entityFactory){
        this.spaceshipSprite = sprite;
        this.entityFactory = entityFactory;

    }

    public CollisionSystem(int priority) {
        super(priority);
    }

    public void addedToEngine(Engine engine) {
        spaceship = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                HealthComponent.class, RotationComponent.class).exclude(ElementComponent.class).get());
        asteroids = engine.getEntitiesFor(Family.all(BodyComponent.class, SpriteComponent.class, HealthComponent.class, ElementComponent.class).get());

        projectiles = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                ElementComponent.class).exclude(HealthComponent.class).get());
        obstacles = engine.getEntitiesFor(Family.all(PositionComponent.class, GravityComponent.class,
                RotationComponent.class).get());
    }

    public void update(float deltaTime) {
        this.boundsSpaceship = new Rectangle(spaceshipSprite.getX(), spaceshipSprite.getY(), spaceshipSprite.getWidth(), spaceshipSprite.getHeight()); // TODO should be calculated directly from Spaceship entity

        for (Entity entity : asteroids) {

            SpriteComponent scom = ComponentMappers.sm.get(entity);
            BodyComponent bcom = ComponentMappers.bm.get(entity);


        }
    }

}
