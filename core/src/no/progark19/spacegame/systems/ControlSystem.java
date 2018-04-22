package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;


import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.utils.EntityFactory;

public class ControlSystem extends EntitySystem {

    private ImmutableArray<Entity> bullets;
    private ImmutableArray<Entity> asteroids;

    boolean testOneBullet = true;
    public float temp;

    private SpaceGame game;
    private EntityFactory entityFactory;

    public ControlSystem(SpaceGame game, EntityFactory entityFactory) {
        this.game = game;
        this.entityFactory = entityFactory;
    }

    public void addedToEngine(Engine engine) {
        bullets = engine.getEntitiesFor(Family.all(
                ElementComponent.class,
                SpriteComponent.class,
                VelocityComponent.class).get());
        }
    //TODO remove asteroid entity if dead
    public void update(float deltaTime) {
        //TODO REMOVE FLIP

    }


}
