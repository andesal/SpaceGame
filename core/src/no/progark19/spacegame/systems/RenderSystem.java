package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.AnimationComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.Paths;

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> spaceshipEntities;
    private static SpriteBatch batch;
    private OrthographicCamera camera;
    private ImmutableArray<Entity> cameraFocusEntity;
    private int bgX = 0;
    private int bgY = 0;
    public Animation animation;
    public Animation animation2;
    private float elapsedTime = 0;
    private float t = 0;

    private ImmutableArray<Entity> animationEntities;
    private ShapeRenderer renderer;
    public SpaceGame game;

    public RenderSystem(SpriteBatch batch, OrthographicCamera camera, ShapeRenderer renderer, SpaceGame game) {
        this.batch = batch;
        this.camera = camera;
        this.renderer = renderer;
        this.game = game;

        animation = AnimationSystem.createAnimation(game.assetManager.get(Paths.FIRE_EXPLOSION_ATLAS, TextureAtlas.class), 1/149f);
        animation2 = AnimationSystem.createAnimation(game.assetManager.get(Paths.ICE_EXPLOSION_ATLAS, TextureAtlas.class), 1/255f);
    }

    @Override
    public void addedToEngine(Engine engine) {
        spaceshipEntities = engine.getEntitiesFor(Family
                .all(RenderableComponent.class, SpriteComponent.class)
                .one(PositionComponent.class, RelativePositionComponent.class).get());
        cameraFocusEntity = engine.getEntitiesFor(Family
                .all(LeadCameraComponent.class, PositionComponent.class)
                .get());
        animationEntities = engine.getEntitiesFor(Family.all(SpriteComponent.class, AnimationComponent.class, RenderableComponent.class, ElementComponent.class).get());

    }

    //Currently only used for debugs in the force-appliersystem
    public static void forceDraw(Sprite sprite){
        sprite.draw(batch);
    }

    @Override
    public void update(float deltaTime) {
        drawBackground();

        updateBackgroundCoordinates();

        for (Entity entity : spaceshipEntities) {
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

        for (Entity entity : animationEntities) {
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
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
            //System.out.println("X: " + camera.position.x);
            //System.out.println("Y: " + camera.position.y);

        };

        t += deltaTime;
        camera.update();
        if (t >= 1) {
            elapsedTime += deltaTime;
            TextureRegion keyFrame = (TextureRegion) animation.getKeyFrame(elapsedTime);
            batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime), camera.position.x - keyFrame.getRegionWidth()/2 ,camera.position.y - keyFrame.getRegionHeight()/2);
            //batch.draw((TextureRegion) animation2.getKeyFrame(elapsedTime),0,0);
            //TODO If is animationfinished: spawn powerup/materials
            System.out.println(animation.isAnimationFinished(elapsedTime));
            TextureRegion r = new TextureRegion();
            //TODO Create assetmanager to prevent memory leaks: https://gamedev.stackexchange.com/questions/113717/confused-about-using-libgdx-dispose

        }

    }

    private void drawBackground() {
        Texture bg = game.assetManager.get(Paths.BACKGROUND_TEXTURE_PATH, Texture.class);
        batch.draw(bg, bgX, bgY);
        batch.draw(bg, bgX - bg.getWidth(), bgY);
        batch.draw(bg, bgX + bg.getWidth(), bgY);

        batch.draw(bg, bgX, bgY - bg.getHeight());
        batch.draw(bg, bgX - bg.getWidth(), bgY - bg.getHeight());
        batch.draw(bg, bgX + bg.getWidth(), bgY - bg.getHeight());

        batch.draw(bg, bgX, bgY + bg.getHeight());
        batch.draw(bg, bgX - bg.getWidth(), bgY + bg.getHeight());
        batch.draw(bg, bgX + bg.getWidth(), bgY + bg.getHeight());
    }

    private void updateBackgroundCoordinates() {
        Texture texture = game.assetManager.get(Paths.BACKGROUND_TEXTURE_PATH);
        int width = texture.getWidth();
        int height = texture.getHeight();
        if (camera.position.x > bgX + width) {
            bgX += width;
        } else if (camera.position.x < bgX) {
            bgX -= height;
        }

        if (camera.position.y > (bgY + height)) {
            bgY += height;
        } else if (camera.position.y < bgY) {
            bgY -= height;
        }
    }



}

