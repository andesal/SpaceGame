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

    private SpaceGame game;
    private ImmutableArray<Entity> entities;

    public AnimationSystem(SpaceGame game) {
        this.game = game;

    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, AnimationComponent.class, RenderableComponent.class, ElementComponent.class).get());

    }

    public void update(float deltaTime) { /*
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
            */
        }



    }

