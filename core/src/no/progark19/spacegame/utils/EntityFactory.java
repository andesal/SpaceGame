package no.progark19.spacegame.utils;



import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.progark19.spacegame.utils.GameSettings;
import com.badlogic.gdx.physics.box2d.World;

import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.AnimationComponent;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.FuelBarComponent;
import no.progark19.spacegame.components.FuelComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.HealthbarComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.components.SynchronizedComponent;
import no.progark19.spacegame.managers.EntityManager;


public class EntityFactory {

    private SpaceGame game;
    private PooledEngine engine;

    public enum ELEMENTS {
        FIRE, ICE
    }

    public enum POWERUPS {
        HEALTH, FUEL
    }

    public EntityFactory(SpaceGame game, PooledEngine engine) {
        this.game = game;
        this.engine = engine;
    }

    public Entity createAsteroid(float x, float y, Vector2 velocity, String element) {
        Entity entity = new Entity();

        ElementComponent ecom = new ElementComponent(element);

        SpriteComponent scom = engine.createComponent(SpriteComponent.class);

        Texture texture;
        if (element.equals("FIRE")) {
            texture = game.assetManager.get(Paths.ASTEROID_FIRE_TEXTURE_PATH, Texture.class);
        } else {
            texture = game.assetManager.get(Paths.ASTEROID_ICE_TEXTURE_PATH, Texture.class);
        }
        scom.sprite = new Sprite(texture);
        scom.sprite.setPosition(x, y);

        if (GameSettings.isNavigator){

            BodyComponent bcom = engine.createComponent(BodyComponent.class);
            //Body body = GameSettings.generatePolygon(x, y, world, texture, null); //polygonsprite parameter not used in method.
            CircleShape shape = new CircleShape();
            shape.setRadius((scom.sprite.getWidth()/2)/GameSettings.BOX2D_PIXELS_TO_METERS);
            short tag = element.equals("FIRE") ? GameSettings.FIRE_ASTEROID_TAG : GameSettings.ICE_ASTEROID_TAG;

            Body body = GameSettings.createDynamicBody(scom.sprite, GameSettings.BOX2D_PHYSICSWORLD, shape,0.5f,0.5f, tag);
            body.setLinearVelocity(velocity);
            bcom.body = body;
            entity.add(bcom);   //Body Component
        } else {
            VelocityComponent vcom = engine.createComponent(VelocityComponent.class);
            vcom.velx = velocity.x;
            vcom.vely = velocity.y;
            entity.add(vcom);
        }

        HealthComponent hcom = new HealthComponent(GameSettings.MAX_HEALTH_ASTEROIDS);
        entity.add(ecom);   //Element Component
        entity.add(scom);
        entity.add(new PositionComponent(x, y));
        entity.add(hcom);
        return entity;
    }


    public Entity createBaseSpaceShip(Stage uiStage) {
        float posx = SpaceGame.WIDTH / 2;
        float posy = SpaceGame.HEIGHT / 2;
        //float posx = RenderSystem.bg.getWidth()/2;
        //float posy = RenderSystem.bg.getHeight()/2;

        Sprite sprite = new Sprite(game.assetManager.get(Paths.SPACESHIP_TEXTURE_PATH, Texture.class));
        sprite.setOriginBasedPosition(posx, posy);

        HealthComponent hcom = new HealthComponent(GameSettings.START_HEALTH);
        /*HealthbarComponent pbcom = engine.createComponent(HealthbarComponent.class);
        pbcom.bar = new MyProgressBar(100, 10, Color.RED);
        pbcom.bar.setPosition(30, Gdx.graphics.getHeight() - 20);
        pbcom.bar.setValue((float) GameSettings.START_HEALTH/100);
        uiStage.addActor(pbcom.bar);*/

        FuelBarComponent fbcom = engine.createComponent(FuelBarComponent.class);
        fbcom.bar = new MyProgressBar(100, 10, Color.GREEN);
        fbcom.bar.setPosition(30, Gdx.graphics.getHeight() - 35);
        fbcom.bar.setValue(GameSettings.START_FUEL/100);
        uiStage.addActor(fbcom.bar);


        FuelComponent fcom = new FuelComponent(GameSettings.START_FUEL);

        if (GameSettings.isNavigator) {
            Body body = GameSettings.createDynamicBody(
                    sprite, GameSettings.BOX2D_PHYSICSWORLD, null,
                    GameSettings.SPACESHIP_DENSITY, GameSettings.SPACESHIP_RESTITUTION, GameSettings.SPACESHIP_TAG);
            return engine.createEntity()
                    .add(new SynchronizedComponent())
                    .add(new PositionComponent(posx, posy))
                    .add(new SpriteComponent(sprite))
                    .add(new BodyComponent(body))
                    .add(new RenderableComponent())
                    .add(new LeadCameraComponent())
                    .add(hcom)
                    .add(fcom)
                    //.add(pbcom)
                    .add(fbcom);
        } else {
            //FOR DEBUGGING
            return engine.createEntity()
                    .add(new PositionComponent(posx, posy))
                    .add(new SpriteComponent(sprite))
                    .add(new RenderableComponent())
                    .add(new LeadCameraComponent())
                    .add(new VelocityComponent())
                    .add(hcom)
                    .add(fcom);
        }
    }


    public Entity createShipEngine(float relx, float rely, float relRot, Entity parent){
        Sprite engineSprite = new Sprite(game.assetManager.get(Paths.ENGINE_TEXTURE_PATH, Texture.class));

        engineSprite.setOrigin(GameSettings.ENGINE_ORIGIN.x, GameSettings.ENGINE_ORIGIN.y);
        engineSprite.setRotation(relRot);

        return engine.createEntity()
                .add(new RelativePositionComponent(relx, rely, relRot))
                .add(new SpriteComponent(engineSprite))
                .add(new ParentComponent(EntityManager.getEntityID(parent)))
                .add(new RenderableComponent())
                .add(new ForceApplierComponent(GameSettings.ENGINE_MAX_FORCE));
    }

    public Entity createProjectile(float x, float y, float velX, float velY, String element) {
        Entity entity = new Entity();

        ElementComponent ecom = new ElementComponent(element);

        Texture texture;
        if (element.equals("FIRE")) {
            texture = game.assetManager.get(Paths.FIRE_BULLET_TEXTURE_PATH, Texture.class);
        } else {
            texture = game.assetManager.get(Paths.ICE_BULLET_TEXTURE_PATH, Texture.class);
        }

        SpriteComponent scom = new SpriteComponent(new Sprite(texture));
        PositionComponent pcom = new PositionComponent(x, y);

        VelocityComponent vcom = new VelocityComponent();
        vcom.velx = velX;
        vcom.vely = velY;
        entity.add(ecom)
                .add(scom)
                .add(vcom)
                .add(pcom)
                .add(new RenderableComponent());
        return entity;
    }

    public Entity createAnimationEntity(float x, float y, String element) {
        Entity entity = new Entity();
        AnimationComponent acom;
        ElementComponent ecom = new ElementComponent(element);
        if (element.equals("FIRE")) {
            acom = new AnimationComponent(GameSettings.createAnimation(game.assetManager.get(Paths.FIRE_EXPLOSION_2_ATLAS, TextureAtlas.class), 1/255f));
        } else {
            acom = new AnimationComponent(GameSettings.createAnimation(game.assetManager.get(Paths.ICE_EXPLOSION_ATLAS, TextureAtlas.class), 1/149f));
        }

        PositionComponent pcom = new PositionComponent(x, y);
        entity.add(acom).add(ecom).add(pcom).add(new RenderableComponent());
        return entity;
    }


    public Entity createPowerup(float x, float y, Enum type) {
        Entity entity = new Entity();

        PowerupComponent pwcom = new PowerupComponent(type);
        SpriteComponent scom = new SpriteComponent();
        Texture texture;
        if (type == POWERUPS.FUEL) {
            texture = game.assetManager.get(Paths.FUEL_TEXTURE_PATH, Texture.class);
        } else {
            texture = game.assetManager.get(Paths.HEALT_TEXTURE_PATH, Texture.class);
        }
        scom.sprite = new Sprite(texture);
        //entity.add(engine.createComponent(RenderableComponent.class));

        PositionComponent pcom = new PositionComponent(x, y);
        entity.add(pwcom).add(scom).add(pcom).add(new RenderableComponent());

        return entity;
    }



}
