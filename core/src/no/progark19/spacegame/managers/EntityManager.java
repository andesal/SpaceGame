package no.progark19.spacegame.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;

import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.GravityComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.SoundComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.systems.CollisionSystem;
import no.progark19.spacegame.systems.ControlSystem;
import no.progark19.spacegame.systems.MovementSystem;
import no.progark19.spacegame.systems.RenderSystem;
import no.progark19.spacegame.systems.SoundSystem;
import no.progark19.spacegame.systems.SpawnSystem;
import no.progark19.spacegame.utils.EntityFactory;


public class EntityManager {

    private PooledEngine engine;
    private World world;
    private OrthographicCamera camera;

    public EntityManager(PooledEngine engine, World world, SpriteBatch batch, OrthographicCamera camera) {
        this.camera = camera;
        this.engine = engine;
        this.world = world;
        createEntities();

        ControlSystem cs = new ControlSystem();
        engine.addSystem(cs);

        RenderSystem rs = new RenderSystem(batch);
        engine.addSystem(rs);

        SpawnSystem ss = new SpawnSystem(engine, camera, new World(new Vector2(0,0), true));
        engine.addSystem(ss);

        MovementSystem ms = new MovementSystem();
        engine.addSystem(ms);

        CollisionSystem cols = new CollisionSystem();
        engine.addSystem(cols);

        SoundSystem sos = new SoundSystem();
        engine.addSystem(sos);


    }

    private void createEntities() {

        EntityFactory ef = new EntityFactory(engine);
        World world = new World(new Vector2(0,0), true);


        //Entity entity = ef.createAsteroid(300, 400, new Vector2(100,100), new Texture("img/fireT.png"), world);

        //engine.addEntity(entity);

        /*
        Entity e = new Entity();
        SpriteComponent scom = engine.createComponent(SpriteComponent.class);
        scom.sprite = new Sprite(new Texture("img/ss.png"));
        e.add(scom);
        PositionComponent p = engine.createComponent(PositionComponent.class);
        e.add(p);

        p.x = 300; p.y = 400;
        e.add(engine.createComponent(RenderableComponent.class));
        engine.addEntity(e);

        Entity asteroid = new Entity()
                .add(new SpriteComponent(new Texture("img/ss.png"), 1))
                .add(new PositionComponent(300, 400))
                .add(new VelocityComponent(1))
                .add(new HealthComponent(100))
                .add(new ElementComponent(ElementComponent.ELEMENTS.FIRE))
                .add(new RotationComponent(10))
                .add(new SoundComponent());
        engine.addEntity(asteroid);

        Entity obstacle = new Entity()
                .add(new SpriteComponent(new Texture("img/placeholder.png"), 1))
                .add(new PositionComponent(200, 200))
                .add(new GravityComponent(10))
                .add(new RotationComponent(10))
                .add(new SoundComponent());

        engine.addEntity(obstacle);

        Entity collectable = new Entity()
                .add(new SpriteComponent(new Texture("img/placeholder.png"), 1))
                .add(new PositionComponent(300, 300))
                .add(new PowerupComponent(PowerupComponent.TYPE.HEALTH, 100))
                .add(new SoundComponent());

        engine.addEntity(collectable);


        Entity motor = new Entity() //x2 or x 4,  Rotation nessecary??
                .add(new SpriteComponent(new Texture("img/placeholder.png"), 1))
                .add(new VelocityComponent(10))
                .add(new RotationComponent(1))
                .add(new SoundComponent());
        engine.addEntity(motor);

        Entity projectile = new Entity() // Rotation nessecary??
                .add(new SpriteComponent(new Texture("img/placeholder.png"), 1))
                .add(new PositionComponent(400, 400))
                .add(new ElementComponent(ElementComponent.ELEMENTS.FIRE))
                .add(new VelocityComponent(1))
                .add(new SoundComponent());
        engine.addEntity(projectile);


        Entity turret = new Entity()
                .add(new SpriteComponent(new Texture("img/placeholder.png"), 1))
                .add(new RotationComponent(10))
                .add(new SoundComponent());
        engine.addEntity(turret);
        */
    }

    public void update() {

        engine.update(Gdx.graphics.getDeltaTime());
    }

}
