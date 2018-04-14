package no.progark19.spacegame.utils;



import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import org.omg.CORBA.Bounds;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.BoundsComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.TextureComponent;

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
        Texture texture = fire ? new Texture("img/fire.png") : new Texture("img/ice.png");
        scom.sprite = new Sprite(texture);
        scom.sprite.setPosition(x, y);

        BodyComponent bcom = engine.createComponent(BodyComponent.class);
        Body body = GameSettings.generatePolygon(x, y, world, texture, null); //polygonsprite parameter not used in method.
        body.setLinearVelocity(velocity);


        bcom.body = body;
        entity.add(bcom);
        entity.add(ecom);
        entity.add(scom);
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


}
