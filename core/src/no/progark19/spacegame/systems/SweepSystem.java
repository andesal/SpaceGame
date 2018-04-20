package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.SweepComponent;

/**
 * Created by anderssalvesen on 20.04.2018.
 */

public class SweepSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;


    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SweepComponent.class).get());
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            getEngine().removeEntity(entity);
        }

    }

}
