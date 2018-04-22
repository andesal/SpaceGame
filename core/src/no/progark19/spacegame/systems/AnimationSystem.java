package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;


import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.AnimationComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;

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

