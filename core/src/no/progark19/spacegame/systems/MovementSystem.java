package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.SpriteComponent;
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

            pcom.rotation = (float) Math.toDegrees(bcom.body.getAngle());
        }

        for (Entity entity : nonBodyEntities){
            VelocityComponent vcom = ComponentMappers.VEL_MAP.get(entity);
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);

            float x = scom.sprite.getX() + vcom.velocity.x;
            float y = scom.sprite.getY() + vcom.velocity.y;

            scom.sprite.setPosition(x, y);


            //pcom.rotation = (float) Math.toDegrees(bcom.body.getAngle());
        }
    }
}
