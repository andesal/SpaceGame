package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Iterator;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.SweepComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.managers.EntityManager;

// Handles the movement of movable objects in the game world

public class MovementSystem extends EntitySystem {
    //private ImmutableArray<Entity> asteroids;
    //private ImmutableArray<Entity> spaceship;
    //private ImmutableArray<Entity> projectiles;
    ImmutableArray<Entity> bodyEntities;
    ImmutableArray<Entity> nonBodyEntities;
    private World world;


    boolean y = true;

    public MovementSystem(World world) {
        this.world = world;
    }

    public MovementSystem(int priority) {
        super(priority);
    }

    public void addedToEngine(Engine engine) {
        bodyEntities = engine.getEntitiesFor(Family
                .all(
                        BodyComponent.class,
                        PositionComponent.class)
                .get());

        nonBodyEntities = engine.getEntitiesFor(Family
                .all(
                        VelocityComponent.class,
                        SpriteComponent.class)
                .get());

        /*
        projectiles = engine.getEntitiesFor(Family
                .all(
                        PositionComponent.class,
                        VelocityComponent.class,
                        ElementComponent.class
                )
                .exclude(HealthComponent.class)
                .get());
        asteroids = engine.getEntitiesFor(Family.all(BodyComponent.class, SpriteComponent.class, HealthComponent.class, ElementComponent.class).get());
        */

    }

    public void update(float deltaTime) {



        Iterator<Entity> i = EntityManager.flaggedForRemoval.iterator();

        if(!world.isLocked()) {
            while(i.hasNext()) {
                Entity entity = i.next();
                Body b = ComponentMappers.BOD_MAP.get(entity).body;
                entity.add(new SweepComponent());
                world.destroyBody(b);
                i.remove();
            }
        }


        world.step(1f/60f, 6,2);

        /*for (Entity entity : asteroids) {
            BodyComponent bcom = ComponentMappers.BOD_MAP.get(entity);
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            Body body = bcom.body;
            float posX = scom.sprite.getX();
            float posY = scom.sprite.getY();
            float velX = body.getLinearVelocity().x;
            float velY = body.getLinearVelocity().y;
            scom.sprite.rotate(0.5f);

            scom.sprite.setPosition(posX += velX * deltaTime, posY += velY * deltaTime);
        }*/

        for (Entity entity : bodyEntities){

            BodyComponent bcom = ComponentMappers.BOD_MAP.get(entity);
            PositionComponent pcom = ComponentMappers.POS_MAP.get(entity);

            pcom.x = bcom.body.getPosition().x * GameSettings.BOX2D_PIXELS_TO_METERS;
            pcom.y = bcom.body.getPosition().y * GameSettings.BOX2D_PIXELS_TO_METERS;

            //Only entities
            if (GameSettings.screenBounds.contains(pcom.x, pcom.y)) {
                entity.add(new RenderableComponent());
            } else {
                entity.remove(RenderableComponent.class);
            }

            pcom.rotation = (float) Math.toDegrees(bcom.body.getAngle());
        }
        for (Entity entity : nonBodyEntities) {
            VelocityComponent vcom = ComponentMappers.VEL_MAP.get(entity);
            PositionComponent pcom = ComponentMappers.POS_MAP.get(entity);

            pcom.x        = pcom.x        + vcom.velx*deltaTime;
            pcom.y        = pcom.y        + vcom.vely*deltaTime;
            pcom.rotation = pcom.rotation + vcom.velAngle*deltaTime;

            //For projectiles
            if (! GameSettings.screenBounds.contains(pcom.x, pcom.y)) {
                entity.remove(RenderableComponent.class);
            }
        }
    }
}
