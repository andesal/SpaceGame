package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.VelocityComponent;

// Handles the movement of movable objects in the game world

public class
MovementSystem extends EntitySystem {

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
        asteroids = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                HealthComponent.class, ElementComponent.class, RotationComponent.class).get());
        projectiles = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                ElementComponent.class).exclude(HealthComponent.class).get());

    }

    public void update(float deltaTime) {
        GameSettings.BOX2D_PHYSICSWORLD.step(1f/60f, 6,2);
    }

}
