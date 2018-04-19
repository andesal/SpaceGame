package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


import java.lang.reflect.GenericArrayType;
import java.util.Locale;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.AnimationComponent;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.utils.EntityFactory;

/**
 * Created by anderssalvesen on 16.04.2018.
 */

public class AnimationSystem extends EntitySystem {

    private Entity fireballTest;
    private SpriteBatch batch;
    private PooledEngine engine;
    private World world;
    private OrthographicCamera camera;

    public Animation largeFireExplosion;// = createAnimation(game.as, 1/255f);
    public Animation largeIceExplosion; //= createAnimation(GameSettings.ICE_EXPLOSION, 1/149f);
    private SpaceGame game;
    private ImmutableArray<Entity> entities;

    public AnimationSystem(EntityFactory entityFactory, SpriteBatch batch, PooledEngine engine, World world, OrthographicCamera camera, SpaceGame game) {
        this.engine = engine;
        this.world = world;
        //fireballTest = entityFactory.createProjectile(1000, 1100, new Vector2(0,0), true, false);
        //engine.addEntity(fireballTest);
        this.game = game;
        this.batch = batch;
        this.camera = camera;


    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, AnimationComponent.class, RenderableComponent.class, ElementComponent.class).get());

    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            AnimationComponent acom = ComponentMappers.ANI_MAP.get(entity);
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            acom.currentFrameTime += deltaTime;
            if (acom.currentFrameTime > acom.maxFrameTime) {
                acom.frame++;
                acom.currentFrameTime = 0;
            }
            if (acom.frame >= acom.frameCount){
                acom.frame = 0;
            }
            scom.sprite.setRegion(acom.getCurrentFrame());

            }
        }

    public static Animation createAnimation(TextureAtlas atlas, float frameDuration) {
        com.badlogic.gdx.utils.Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        TextureAtlas.AtlasRegion[] frames = new TextureAtlas.AtlasRegion[regions.size];
        for(int i = 0; i < frames.length; i++) {
            frames[i] = atlas.findRegion("explosion" + String.format(Locale.getDefault(), "%04d", Integer.parseInt(String.valueOf(i))));
        }
        return new Animation<TextureRegion>(frameDuration, frames);
    }

    }

