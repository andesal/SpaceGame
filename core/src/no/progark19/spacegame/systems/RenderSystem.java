package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


import java.util.HashMap;

import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.AnimationComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.Paths;

public class RenderSystem extends EntitySystem {
    private int bgX = 0;
    private int bgY = 0;
    public Animation animation;
    public Animation animation2;
    private float elapsedTime = 0;
    private float t = 0;

    private HashMap<Entity, Float> stateTimes;
    private ImmutableArray<Entity> renderables;
    private ImmutableArray<Entity> cameraFocusEntity;
    private ImmutableArray<Entity> explosionEntities;
    private ImmutableArray<Entity> bulletEntities;

    private SpaceGame game;
    private static SpaceGame game1; //For debug forcedraw...

    public RenderSystem(SpaceGame game) {
        this.game = game;
        this.game1 = game; //For debug forcedraw
        stateTimes = new HashMap<Entity, Float>();
    }

    @Override
    public void addedToEngine(Engine engine) {
        renderables = engine.getEntitiesFor(Family
                .all(RenderableComponent.class, SpriteComponent.class)
                .one(PositionComponent.class, RelativePositionComponent.class).get());
        cameraFocusEntity = engine.getEntitiesFor(Family
                .all(LeadCameraComponent.class, PositionComponent.class)
                .get());
        explosionEntities = engine.getEntitiesFor(Family.one(AnimationComponent.class).get());
        bulletEntities = engine.getEntitiesFor(Family.all(
                SpriteComponent.class,
                ElementComponent.class,
                RenderableComponent.class).get());

    }

    //Currently only used for debugs in the force-appliersystem
    public static void forceDraw(Sprite sprite){
        sprite.draw(game1.batch);
    }

    @Override
    public void update(float deltaTime) {
        drawBackground();

        updateBackgroundCoordinates();

        for (Entity entity : renderables) {

            if (ComponentMappers.LEAD_MAP.get(entity) != null) {
                System.out.println("HEALTH = " + ComponentMappers.HEALTH_MAP.get(entity).health+ " : " + ComponentMappers.FUEL_MAP.get(entity).fuel + " = " + "FUEL");
            }

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
            scom.sprite.draw(game.batch);
        }

        //TODO Change so that bullets are drawn in renderables...
        for (Entity entity : bulletEntities) {
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            scom.sprite.draw(game.batch);
        }

        for (Entity entity : explosionEntities) {
            if (stateTimes.get(entity) == null) {
                stateTimes.put(entity, 0f);
            }
            float temp = stateTimes.get(entity);
            stateTimes.put(entity, temp + deltaTime);
            PositionComponent pcom = ComponentMappers.POS_MAP.get(entity);
            AnimationComponent acom = ComponentMappers.ANI_MAP.get(entity);
            TextureRegion keyFrame = (TextureRegion) acom.animation.getKeyFrame(stateTimes.get(entity));
            game.batch.draw((TextureRegion) acom.animation.getKeyFrame(stateTimes.get(entity)), pcom.x - keyFrame.getRegionWidth()/2 ,pcom.y - keyFrame.getRegionHeight()/2);
            if (acom.animation.isAnimationFinished(stateTimes.get(entity))) {
                stateTimes.remove(entity);
                getEngine().removeEntity(entity);
            }
        }
        if (GameSettings.CAMERA_FOLLOW_POSITION){

            PositionComponent pcom = ComponentMappers.POS_MAP.get(cameraFocusEntity.get(0));
            game.camera.position.set(pcom.x, pcom.y, 0);

            if (GameSettings.CAMERA_FOLLOW_ROTATION){
                game.camera.up.set(0,1,0);
                game.camera.direction.set(0,0,-1);
                game.camera.rotate(-pcom.rotation);
            }
            game.camera.update();
            //System.out.println("X: " + game.camera.position.x);
            //System.out.println("Y: " + game.camera.position.y);

        };
    }

    private void drawBackground() {
        Texture bg = game.assetManager.get(Paths.BACKGROUND_TEXTURE_PATH, Texture.class);
        game.batch.draw(bg, bgX, bgY);
        game.batch.draw(bg, bgX - bg.getWidth(), bgY);
        game.batch.draw(bg, bgX + bg.getWidth(), bgY);

        game.batch.draw(bg, bgX, bgY - bg.getHeight());
        game.batch.draw(bg, bgX - bg.getWidth(), bgY - bg.getHeight());
        game.batch.draw(bg, bgX + bg.getWidth(), bgY - bg.getHeight());

        game.batch.draw(bg, bgX, bgY + bg.getHeight());
        game.batch.draw(bg, bgX - bg.getWidth(), bgY + bg.getHeight());
        game.batch.draw(bg, bgX + bg.getWidth(), bgY + bg.getHeight());
    }

    private void updateBackgroundCoordinates() {
        Texture texture = game.assetManager.get(Paths.BACKGROUND_TEXTURE_PATH, Texture.class);
        int width = texture.getWidth();
        int height = texture.getHeight();
        if (game.camera.position.x > bgX + width) {
            bgX += width;
        } else if (game.camera.position.x < bgX) {
            bgX -= width;
        }

        if (game.camera.position.y > (bgY + height)) {
            bgY += height;

        } else if (game.camera.position.y < bgY) {
            bgY -= height;

        }
    }



}

