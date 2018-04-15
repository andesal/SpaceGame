package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.managers.EntityManager;

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private SpriteBatch batch;

    public RenderSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family
                .all(RenderableComponent.class, SpriteComponent.class)
                .one(PositionComponent.class, RelativePositionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            //FIXME Kanskje fjerne positioncomponent og kun bruke sprites?
            PositionComponent pcom = ComponentMappers.POS_MAP.get(entity);
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);

            if (pcom != null) {
                scom.sprite.setOriginBasedPosition(pcom.x,pcom.y);
                scom.sprite.setRotation(pcom.rotation);

            } else {
                RelativePositionComponent relcom = ComponentMappers.RELPOS_MAP.get(entity);
                ParentComponent parcom = ComponentMappers.PARENT_MAP.get(entity);
                pcom = ComponentMappers.POS_MAP.get(EntityManager.getEntity(parcom.parentID));

                float relX = pcom.x + relcom.x;
                float relY = pcom.y + relcom.y;
                float relRot = pcom.rotation + relcom.rotation;
                scom.sprite.setOriginBasedPosition(relX, relY);
                scom.sprite.setRotation(relRot);
            }

            scom.sprite.draw(batch);
        }
    }
}

