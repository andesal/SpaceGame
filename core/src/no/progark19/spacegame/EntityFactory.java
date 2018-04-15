package no.progark19.spacegame;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.managers.EntityManager;

/**
 * Created by Anders on 13.04.2018.
 */

public class EntityFactory {
    private PooledEngine engine;

    public EntityFactory(PooledEngine engine) {
        this.engine = engine;
    }

    public Entity createBaseSpaceShip(World physicsWorld, Texture texture){
        float posx = SpaceGame.WIDTH/2;
        float posy = SpaceGame.HEIGHT/2;

        Sprite sprite = new Sprite(texture);
        sprite.setOriginBasedPosition(posx, posy);

        Body body = GameSettings.createDynamicBody(
                sprite, physicsWorld, null,
                GameSettings.SPACESHIP_DENSITY, GameSettings.SPACESHIP_RESTITUTION
        );

        return engine.createEntity()
                .add(new PositionComponent(posx, posy))
                .add(new SpriteComponent(sprite))
                .add(new BodyComponent(body))
                .add(new RenderableComponent());
    }

    public Entity createShipEngine(float relx, float rely, float relRot, Entity parent, Texture texture){
        Sprite engineSprite = new Sprite(texture);

        engineSprite.setOrigin(GameSettings.ENGINE_ORIGIN.x, GameSettings.ENGINE_ORIGIN.y);
        engineSprite.setRotation(relRot);

        return engine.createEntity()
                .add(new RelativePositionComponent(relx, rely, relRot))
                .add(new SpriteComponent(engineSprite))
                .add(new ParentComponent(EntityManager.getEntityID(parent)))
                .add(new RenderableComponent())
                .add(new ForceApplierComponent(GameSettings.ENGINE_MAX_FORCE));
    }

}
