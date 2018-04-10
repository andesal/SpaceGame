package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private SpriteBatch batch;

    public RenderSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(RenderableComponent.class, SpriteComponent.class, PositionComponent.class).get());
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            SpriteComponent scom = ComponentMappers.sm.get(entity);
            PositionComponent pcom = ComponentMappers.pm.get(entity);
            scom.sprite.setPosition(pcom.x,pcom.y);
            scom.sprite.draw(batch);
        }
    }
}

