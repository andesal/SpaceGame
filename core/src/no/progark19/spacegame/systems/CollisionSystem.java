package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.GravityComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.VelocityComponent;

/**
 * Created by anderssalvesen on 10.04.2018.
 */

public class CollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> spaceship;
    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> projectiles;
    private ImmutableArray<Entity> obstacles;

    public CollisionSystem(){}

    public CollisionSystem(int priority) {
        super(priority);
    }

    public void addedToEngine(Engine engine) {
        spaceship = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                HealthComponent.class, RotationComponent.class).exclude(ElementComponent.class).get());
        asteroids = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                HealthComponent.class, ElementComponent.class, RotationComponent.class).get());
        projectiles = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                ElementComponent.class).exclude(HealthComponent.class).get());
        obstacles = engine.getEntitiesFor(Family.all(PositionComponent.class, GravityComponent.class,
                RotationComponent.class).get());
    }

    public void update(float deltaTime) {
        /*for (Entity entity : spaceship) {
            PositionComponent pos = ComponentMappers.POS_MAP.get(entity);
        }*/
    }

}
