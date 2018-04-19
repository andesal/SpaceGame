package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.awt.Rectangle;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.AnimationComponent;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.utils.Calc;
import no.progark19.spacegame.utils.EntityFactory;

// Handles controllable objects in the game world

public class ControlSystem extends EntitySystem {

    private ImmutableArray<Entity> bullets;
    private ImmutableArray<Entity> asteroids;


    float temp = 0;

    private Rectangle screenBounds;
    private PooledEngine engine;
    private OrthographicCamera camera;
    private EntityFactory entityFactory;

    public ControlSystem(OrthographicCamera camera, EntityFactory entityFactory, PooledEngine engine) {
        this.engine = engine;
        this.camera = camera;
        this.entityFactory = entityFactory;

        this.screenBounds = new Rectangle();

    }

    public ControlSystem(int priority) {
        super(priority);
    }

    public void addedToEngine(Engine engine) {
        bullets = engine.getEntitiesFor(Family.all(
                ElementComponent.class,
                AnimationComponent.class,
                SpriteComponent.class,
                VelocityComponent.class).get());
    }
    //TODO remove asteroid entity if dead
    public void update(float deltaTime) {
        if (bullets.size() == 0) {
            Entity entity = entityFactory.createProjectile(camera.position.x, camera.position.y, new Vector2(10,0), true, true, 0);
            this.engine.addEntity(entity);
        } else {
            screenBounds.setBounds((int) camera.position.x - (SpaceGame.WIDTH / 2), (int) camera.position.y - (SpaceGame.HEIGHT / 2), SpaceGame.WIDTH, SpaceGame.HEIGHT);
            temp += deltaTime;
            if (temp > 1) {
                for (Entity entity : bullets) {
                    SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
                    entity.add(new RenderableComponent());
                    if (!screenBounds.contains(scom.sprite.getX(), scom.sprite.getY())) {
                        engine.removeEntity(entity);
                        //entityFactory.createProjectile(camera.position.x, camera.position.y, new Vector2(0, 0), true, false, 0);
                    }

                }

            }
        }


    }


}
