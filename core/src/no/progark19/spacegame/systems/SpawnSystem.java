package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.GravityComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.VelocityComponent;

public class SpawnSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> obstacles;
    private ImmutableArray<Entity> collectables;


    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    private ComponentMapper<ElementComponent> em = ComponentMapper.getFor(ElementComponent.class);
    private ComponentMapper<RotationComponent> rm = ComponentMapper.getFor(RotationComponent.class);
    private ComponentMapper<PowerupComponent> pom = ComponentMapper.getFor(PowerupComponent.class);
    private ComponentMapper<GravityComponent> gm = ComponentMapper.getFor(GravityComponent.class);

    public SpawnSystem() {}

    public SpawnSystem(int priority) {
        super(priority);
    }

    public void addedToEngine(Engine engine) {
        asteroids = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class,
                HealthComponent.class, ElementComponent.class, RotationComponent.class).get());
        obstacles = engine.getEntitiesFor(Family.all(PositionComponent.class, GravityComponent.class,
                RotationComponent.class).get());
        collectables = engine.getEntitiesFor(Family.all(PositionComponent.class,
                PowerupComponent.class).get());

    }

    public void update(float deltaTime) {
        for (Entity entity : collectables) {
            //do something
        }
    }

}
