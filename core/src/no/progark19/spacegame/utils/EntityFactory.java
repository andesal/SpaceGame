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

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.TextureComponent;

/**
 * Created by Anders on 14.04.2018.
 */

public class EntityFactory {

        private PooledEngine engine;

        public EntityFactory(PooledEngine engine) {
            this.engine = engine;
        }


    public Entity createAsteroid(float x, float y, Vector2 velocity, Texture texture, World world) {
        Entity entity = new Entity();

        SpriteComponent scom = engine.createComponent(SpriteComponent.class);
        scom.sprite = new Sprite(texture);
        scom.sprite.setPosition(x, y);

        BodyComponent bcom = engine.createComponent(BodyComponent.class);
        Body body = GameSettings.generatePolygon(x, y, world, texture, null); //polygonsprite parameter not used in method.
        body.setLinearVelocity(velocity);

        bcom.body = body;
        entity.add(bcom);
        entity.add(scom);
        entity.add(engine.createComponent(HealthComponent.class));
        entity.add(engine.createComponent(ElementComponent.class));
        entity.add(engine.createComponent(RenderableComponent.class));
        return entity;
    }


}
