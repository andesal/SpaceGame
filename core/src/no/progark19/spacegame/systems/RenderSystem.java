package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.managers.EntityManager;

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private static SpriteBatch batch;
    private OrthographicCamera cam;
    private ImmutableArray<Entity> cameraFocusEntity;

    public RenderSystem(SpriteBatch batch, OrthographicCamera cam) {
        this.batch = batch;
        this.cam = cam;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family
                .all(RenderableComponent.class, SpriteComponent.class)
                .one(PositionComponent.class, RelativePositionComponent.class).get());
        cameraFocusEntity = engine.getEntitiesFor(Family
                .all(LeadCameraComponent.class, PositionComponent.class)
                .get());
    }

    //Currently only used for debugs in the force-appliersystem
    public static void forceDraw(Sprite sprite){
        sprite.draw(batch);
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

                Vector2 relPos = new Vector2(relcom.x, relcom.y);
                Vector2 newRelPos = relPos.cpy().setAngle(relPos.angle() + pcom.rotation);

                float relX = pcom.x + newRelPos.x;
                float relY = pcom.y + newRelPos.y;

                float relRot = relcom.rotation + pcom.rotation;

                scom.sprite.setOriginBasedPosition(relX, relY);
                scom.sprite.setRotation(relRot);
            }
            scom.sprite.draw(batch);
        }
        if (GameSettings.CAMERA_FOLLOW_POSITION){

            PositionComponent pcom = ComponentMappers.POS_MAP.get(cameraFocusEntity.get(0));
            cam.position.set(pcom.x, pcom.y, 0);

            if (GameSettings.CAMERA_FOLLOW_ROTATION){
                cam.up.set(0,1,0);
                cam.direction.set(0,0,-1);
                cam.rotate(-pcom.rotation);
            }
            cam.update();

        }
    }
}

