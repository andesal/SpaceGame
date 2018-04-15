package no.progark19.spacegame.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

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


public class EntityManager implements EntityListener{
    private int entityID = 0;

    private Engine engine;

    private static HashMap<Integer, Entity> integerEntityMap = new HashMap<Integer, Entity>();
    private static HashMap<Entity, Integer> entityIntegerMap = new HashMap<Entity, Integer>();

    public EntityManager() {
    }

    public EntityManager(Engine e, SpriteBatch batch) {
        engine = e;

        ControlSystem cs = new ControlSystem();
        engine.addSystem(cs);

        RenderSystem rs = new RenderSystem(batch);
        engine.addSystem(rs);

        SpawnSystem ss = new SpawnSystem();
        engine.addSystem(ss);

        MovementSystem ms = new MovementSystem();
        engine.addSystem(ms);

        CollisionSystem cols = new CollisionSystem();
        engine.addSystem(cols);

        SoundSystem sos = new SoundSystem();
        engine.addSystem(sos);

        createEntities();

    }

    private void createEntities() {

        Entity spaceship = new Entity()
                .add(new SpriteComponent(new Texture("img/ss.png"), 0.5f))
                .add(new PositionComponent(SpaceGame.WIDTH / 16, SpaceGame.HEIGHT / 2))
                .add(new VelocityComponent(3))
                .add(new RotationComponent(10))
                .add(new HealthComponent(100))
                .add(new SoundComponent("data/engine.mp3", 1f, 1f, 1f))
                .add(new RenderableComponent());

        engine.addEntity(spaceship);


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

    }

    public void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void entityAdded(Entity entity) {
        integerEntityMap.put(entityID, entity);
        entityIntegerMap.put(entity, entityID);
        entityID ++;
        System.out.println("Entity added");
    }

    @Override
    public void entityRemoved(Entity entity) {
        integerEntityMap.remove(entityIntegerMap.remove(entity));
        System.out.println("Entity removed");
    }

    public static Entity getEntity(int id){
        return integerEntityMap.get(id);
    }

    public static int getEntityID(Entity entity){
        return entityIntegerMap.get(entity);
    }

}
