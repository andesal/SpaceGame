package no.progark19.spacegame.utils;



import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import org.omg.CORBA.Bounds;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.AnimationComponent;
//import no.progark19.spacegame.components.AsteroidComponent;
import no.progark19.spacegame.components.BodyComponent;
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
import no.progark19.spacegame.components.SynchronizedComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.systems.RenderSystem;

/**
 * Created by Anders on 14.04.2018.
 */

public class EntityFactory {

    private SpaceGame game;
    private PooledEngine engine;
    public enum ELEMENTS {
        FIRE, ICE, SPECIAL
    }

    public EntityFactory(SpaceGame game, PooledEngine engine) {
        this.game = game;
        this.engine = engine;
    }

    public Entity createAsteroid(float x, float y, Vector2 velocity, Enum element) {
        Entity entity = new Entity();

        ElementComponent ecom = new ElementComponent(element);

        SpriteComponent scom = engine.createComponent(SpriteComponent.class);

        Texture texture;
        if (element == ELEMENTS.FIRE) {
            texture = game.assetManager.get(Paths.ASTEROID_FIRE_TEXTURE_PATH, Texture.class);
        } else {
            texture = game.assetManager.get(Paths.ASTEROID_ICE_TEXTURE_PATH, Texture.class);
        }
        scom.sprite = new Sprite(texture);
        scom.sprite.setPosition(x, y);

        if (GameSettings.isPhysicsResponsible){

            BodyComponent bcom = engine.createComponent(BodyComponent.class);
            //Body body = GameSettings.generatePolygon(x, y, world, texture, null); //polygonsprite parameter not used in method.
            CircleShape shape = new CircleShape();
            shape.setRadius((scom.sprite.getWidth()/2)/GameSettings.BOX2D_PIXELS_TO_METERS);
            short tag = element == ELEMENTS.FIRE ? GameSettings.FIRE_ASTEROID_TAG : GameSettings.ICE_ASTEROID_TAG;

            Body body = GameSettings.createDynamicBody(scom.sprite, world, shape,0.5f,0.5f, tag);
            body.setLinearVelocity(velocity);
            bcom.body = body;
            entity.add(bcom);   //Body Component
        } else {
            VelocityComponent vcom = engine.createComponent(VelocityComponent.class);
            vcom.velx = velocity.x;
            vcom.vely = velocity.y;
            entity.add(vcom);
        }



        entity.add(ecom);   //Element Component
        entity.add(scom);
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

        if (GameSettings.isPhysicsResponsible){
            Body body = GameSettings.createDynamicBody(sprite, physicsWorld, null,
                    GameSettings.SPACESHIP_DENSITY, GameSettings.SPACESHIP_RESTITUTION,
                    GameSettings.SPACESHIP_TAG);
            return engine.createEntity()
                    .add(new SynchronizedComponent())
                    .add(new PositionComponent(posx, posy))
                    .add(new SpriteComponent(sprite))
                    .add(new BodyComponent(body))
                    .add(new RenderableComponent())
                    .add(new LeadCameraComponent())
                    .add(new HealthComponent());
        } else {
            return engine.createEntity()
                    .add(new PositionComponent(posx, posy))
                    .add(new SpriteComponent(sprite))
                    .add(new RenderableComponent())
                    .add(new LeadCameraComponent())
                    .add(new VelocityComponent())
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

    public Entity createProjectile(float x, float y, Vector2 velocity, Enum element, boolean flip, float rotation) {
        Entity entity = new Entity();

        ElementComponent ecom = new ElementComponent(element);

        Texture texture;
        if (element == ELEMENTS.FIRE) {
            texture = game.assetManager.get(Paths.FIRE_BULLET_TEXTURE_PATH, Texture.class);
        } else {
            texture = game.assetManager.get(Paths.ICE_BULLET_TEXTURE_PATH, Texture.class);
        }

        SpriteComponent scom = new SpriteComponent(new Sprite(texture));
        scom.sprite.setPosition(x,y);

        VelocityComponent vcom = new VelocityComponent(velocity);


        entity.add(ecom).add(scom).add(vcom).add(new RenderableComponent());
        return entity;
    }

    public Entity createAnimationEntity(float x, float y, Enum element) {
        Entity entity = new Entity();
        AnimationComponent acom;
        ElementComponent ecom = new ElementComponent(element);
        if (element == ELEMENTS.FIRE) {
            acom = new AnimationComponent(GameSettings.createAnimation(game.assetManager.get(Paths.FIRE_EXPLOSION_2_ATLAS, TextureAtlas.class), 1/255f));
        } else {
            acom = new AnimationComponent(GameSettings.createAnimation(game.assetManager.get(Paths.ICE_EXPLOSION_ATLAS, TextureAtlas.class), 1/149f));
        }

        PositionComponent pcom = new PositionComponent(x, y);
        entity.add(acom).add(ecom).add(pcom).add(new RenderableComponent());
        return entity;
    }



}
