package no.progark19.spacegame.utils;



import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.AnimationComponent;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.BoundsComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.TextureComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.screens.PlayScreen;
import no.progark19.spacegame.systems.RenderSystem;

/**
 * Created by Anders on 14.04.2018.
 */

public class EntityFactory {

    private PooledEngine engine;
    public enum ELEMENTS {
        FIRE, ICE, SPECIAL
    }

    public EntityFactory(PooledEngine engine) {
            this.engine = engine;
        }

    // TODO Trenger vi elementComponent?
    public Entity createAsteroid(float x, float y, Vector2 velocity, World world, boolean fire) {
        Entity entity = new Entity();

        ElementComponent ecom = engine.createComponent(ElementComponent.class);
        ecom.element = fire ? ELEMENTS.FIRE : ELEMENTS.ICE;

        SpriteComponent scom = engine.createComponent(SpriteComponent.class);
        Texture texture = fire ? new Texture(GameSettings.ASTEROID_FIRE_TEXTURE_PATH) : new Texture(GameSettings.ASTEROID_ICE_TEXTURE_PATH);
        scom.sprite = new Sprite(texture);
        scom.sprite.setPosition(x, y);

        BodyComponent bcom = engine.createComponent(BodyComponent.class);
        //Body body = GameSettings.generatePolygon(x, y, world, texture, null); //polygonsprite parameter not used in method.
        CircleShape shape = new CircleShape();
        shape.setRadius((scom.sprite.getWidth()/2)/GameSettings.BOX2D_PIXELS_TO_METERS);
        short tag = fire ? GameSettings.FIRE_ASTEROID_TAG : GameSettings.ICE_ASTEROID_TAG;

        Body body = GameSettings.createDynamicBody(scom.sprite, world, shape,0.5f,0.5f, tag);
        body.setLinearVelocity(velocity);

        bcom.body = body;
        entity.add(bcom);   //Body Component
        entity.add(ecom);   //Element Component
        entity.add(scom);   //Sprite Component
        entity.add(new PositionComponent(x, y));
        entity.add(engine.createComponent(HealthComponent.class));
        entity.add(engine.createComponent(RenderableComponent.class));
        return entity;
    }

    public Entity createPowerup(float x, float y, Texture texture) {
        Entity entity = new Entity();
        SpriteComponent scom = engine.createComponent(SpriteComponent.class);
        scom.sprite = new Sprite(texture);
        scom.sprite.setPosition(x, y);
        entity.add(scom);

        entity.add(engine.createComponent(PowerupComponent.class));
        entity.add(engine.createComponent(RenderableComponent.class));

        entity.add(engine.createComponent(BodyComponent.class));

        return entity;
    }

    public Entity createBaseSpaceShip(World physicsWorld, Texture texture){
        float posx = SpaceGame.WIDTH/2;
        float posy = SpaceGame.HEIGHT/2;
        //float posx = RenderSystem.bg.getWidth()/2;
        //float posy = RenderSystem.bg.getHeight()/2;

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
                .add(new RenderableComponent())
                .add(new LeadCameraComponent())
                .add(new HealthComponent());
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

    public Entity createProjectile(float x, float y, Vector2 velocity, boolean fire, boolean flip, float rotation) {
        //TODO ROTATION NOT WORKING
        Entity entity = new Entity();

        ElementComponent ecom = engine.createComponent(ElementComponent.class);
        ecom.element = fire ? ELEMENTS.FIRE : ELEMENTS.ICE;

        Texture texture = fire ? new Texture(GameSettings.FIRE_PROJECTILE_REGION) : new Texture(GameSettings.ICE_PROJECTILE_REGION);
        TextureRegion region = new TextureRegion(texture);
        AnimationComponent acom = new AnimationComponent(region, 2,3, 0.5f, flip);
        TextureRegion regionFrame = acom.getCurrentFrame();
        Texture frame = regionFrame.getTexture();

        SpriteComponent scom = new SpriteComponent(new Sprite(frame));
        scom.sprite.setRegion(acom.getCurrentFrame());
        if (flip) {
            scom.sprite.setPosition(x,y);
        } else {
            scom.sprite.setPosition(x,y);
        }
        VelocityComponent vcom = new VelocityComponent(velocity);

        entity.add(ecom).add(acom).add(scom).add(vcom);
        return entity;
    }



}
