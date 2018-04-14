package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.TextureComponent;

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private SpriteBatch batch;

    public RenderSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(BodyComponent.class, RenderableComponent.class, SpriteComponent.class).get());
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            BodyComponent bcom = ComponentMappers.bm.get(entity);
            SpriteComponent scom = ComponentMappers.sm.get(entity);
            scom.sprite.draw(batch);
        }
    }
}

