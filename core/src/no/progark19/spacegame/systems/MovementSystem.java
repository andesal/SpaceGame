package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;

import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.managers.EntityManager;

// Handles the movement of movable objects in the game world

public class MovementSystem extends EntitySystem {

    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> spaceship;
    private ImmutableArray<Entity> projectiles;

    public MovementSystem() {}

    public MovementSystem(int priority) {
        super(priority);
    }

    public void addedToEngine(Engine engine) {
        spaceship = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                HealthComponent.class, RotationComponent.class).exclude(ElementComponent.class).get());

        projectiles = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                ElementComponent.class).exclude(HealthComponent.class).get());
        asteroids = engine.getEntitiesFor(Family.all(BodyComponent.class, SpriteComponent.class, HealthComponent.class, ElementComponent.class).get());

    }

    public void update(float deltaTime) {
        for (Entity entity : asteroids) {
            BodyComponent bcom = ComponentMappers.bm.get(entity);
            SpriteComponent scom = ComponentMappers.sm.get(entity);
            Body body = bcom.body;
            float posX = scom.sprite.getX();
            float posY = scom.sprite.getY();
            float velX = body.getLinearVelocity().x;
            float velY = body.getLinearVelocity().y;
            scom.sprite.rotate(0.5f);

            scom.sprite.setPosition(posX += velX * deltaTime, posY += velY * deltaTime);
        }

    }

}
