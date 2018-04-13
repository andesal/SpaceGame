package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.VelocityComponent;

// Handles controllable objects in the game world

public class ControlSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> spaceship;
    //private ImmutableArray<Entity> turret;

    public ControlSystem() {}

    public ControlSystem(int priority) {
        super(priority);
    }

    public void addedToEngine(Engine engine) {
        spaceship = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                HealthComponent.class, RotationComponent.class).exclude(ElementComponent.class).get());
        //turret = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
        //entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    public void update(float deltaTime) {
        for (Entity entity : spaceship) {
            PositionComponent position = ComponentMappers.pm.get(entity);
            VelocityComponent velocity = ComponentMappers.vm.get(entity);
        }
    }
}
