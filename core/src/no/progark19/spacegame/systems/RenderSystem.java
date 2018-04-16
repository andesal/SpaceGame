package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    private OrthographicCamera camera;
    private ImmutableArray<Entity> cameraFocusEntity;
    public static Texture bg = new Texture("img/bg1.jpg");

    private int bgX = 0;
    private int bgY = 0;


    public RenderSystem(SpriteBatch batch, OrthographicCamera camera) {
        this.batch = batch;
        this.camera = camera;
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
        batch.draw(bg, bgX, bgY);

        batch.draw(bg, bgX- bg.getWidth(), bgY);
        batch.draw(bg, bgX+ bg.getWidth(), bgY);

        batch.draw(bg, bgX -bg.getWidth(), bgY + bg.getHeight());
        batch.draw(bg, bgX + bg.getWidth(), bgY - bg.getHeight());

        batch.draw(bg, bgX, bgY + bg.getHeight());
        batch.draw(bg, bgX, bgY - bg.getHeight());

        batch.draw(bg, bgX - bg.getWidth(), bgY - bg.getHeight());
        batch.draw(bg, bgX + bg.getWidth(), bgY - bg.getWidth());




        //batch.draw(bg, bgX - bg.getWidth(), bgY);
        //batch.draw(bg, bgX - bg.getWidth(), bgY);

        //batch.draw(bg, bgX -bg.getWidth(), bgY - bg.getHeight());
        //batch.draw(bg, bgX, bgY - bg.getHeight());
        updateBackgroundCoordinates();
        System.out.println("CAM: " + camera.position.x + " : " + bgX);
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

                /*

                relativePosition.setAngle(degrees + relativeAngle);
                rotation = ((engineRotation % 360) + (degrees % 360));
                */

                float relRot = relcom.rotation + pcom.rotation;

                scom.sprite.setOriginBasedPosition(relX, relY);
                scom.sprite.setRotation(relRot);
            }
            scom.sprite.draw(batch);
        }
        if (GameSettings.CAMERA_FOLLOW_POSITION){

            PositionComponent pcom = ComponentMappers.POS_MAP.get(cameraFocusEntity.get(0));
            camera.position.set(pcom.x, pcom.y, 0);

            if (GameSettings.CAMERA_FOLLOW_ROTATION){
                camera.up.set(0,1,0);
                camera.direction.set(0,0,-1);
                camera.rotate(-pcom.rotation);
            }
            camera.update();

        };
    }

    private void updateBackgroundCoordinates() {
        if (camera.position.x > bgX + bg.getWidth()) {
            bgX += bg.getWidth();
            System.out.println("plus X");
        }

        if (camera.position.x < bgX) {
            bgX -= bg.getWidth();
            System.out.println("minus X");

        }

        if (camera.position.y > (bgY + bg.getHeight())) {
            bgY += bg.getHeight();
            System.out.println("plus Y");

        } else if (camera.position.y < bgY) {
            bgY -= bg.getHeight();
            System.out.println("minus Y");

        }
    }
}

