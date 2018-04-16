package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.GravityComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.utils.EntityFactory;

public class SpawnSystem extends EntitySystem {

    private PooledEngine engine;
    private EntityFactory entityFactory;
    private World world;
    boolean fire = true;

    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> obstacles;
    private ImmutableArray<Entity> collectables;
    private OrthographicCamera camera;
    private int numAsteroids = 30;

    private Rectangle spawn = new Rectangle();
    private Rectangle notSpawn = new Rectangle();

    /*private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    private ComponentMapper<ElementComponent> em = ComponentMapper.getFor(ElementComponent.class);
    private ComponentMapper<RotationComponent> rm = ComponentMapper.getFor(RotationComponent.class);
    private ComponentMapper<PowerupComponent> pom = ComponentMapper.getFor(PowerupComponent.class);
    private ComponentMapper<GravityComponent> gm = ComponentMapper.getFor(GravityComponent.class);
    */

    public SpawnSystem(PooledEngine engine, OrthographicCamera camera, World world, EntityFactory entityFactory) {
        this.engine = engine;
        this.entityFactory = entityFactory;
        this.world = world;
        this.camera = camera;
    }


    public void addedToEngine(Engine engine) {
        asteroids = engine.getEntitiesFor(Family.all(BodyComponent.class, SpriteComponent.class, HealthComponent.class, ElementComponent.class).get());
        obstacles = engine.getEntitiesFor(Family.all(PositionComponent.class, GravityComponent.class,
                RotationComponent.class).get());
        collectables = engine.getEntitiesFor(Family.all(PositionComponent.class,
                PowerupComponent.class).get());


    }

    public void update(float deltaTime) {
        // TODO Calculate angle + velocity of added asteroids to move towards spaceship projected location
        Random r = new Random();
        notSpawn = new Rectangle(camera.position.x - 720, camera.position.y - 1080, 1440, 2160);
        spawn = new Rectangle(notSpawn.x - 480, notSpawn.y - 720, 2400, 3600);

        while (numAsteroids < 30) {
            int x = calculateSpawnCoordinates(r).get(0);
            int y = calculateSpawnCoordinates(r).get(1);
            float velX = randomNumber(r, -200,200)/ GameSettings.BOX2D_PIXELS_TO_METERS;
            float velY = randomNumber(r, -200,200)/ GameSettings.BOX2D_PIXELS_TO_METERS;
            fire = fire == true ? false : true;
            engine.addEntity(entityFactory.createAsteroid(x,y,new Vector2(velX, velY), world, fire));
            numAsteroids++;

        }

        for (Entity entity : asteroids) {
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            float x = scom.sprite.getX();
            float y = scom.sprite.getY();
            if (! spawn.contains(x, y)) {
                engine.removeEntity(entity);
                numAsteroids--;

            }
        }
    }


    private ArrayList<Integer> calculateSpawnCoordinates(Random r) {
        int LeftX = randomNumber(r, (int) spawn.x, (int) notSpawn.x);
        int RightX = randomNumber(r, (int) (notSpawn.x + notSpawn.getWidth()), (int) (spawn.x + spawn.getWidth()));
        int bottomY = randomNumber(r, (int) spawn.y, (int) notSpawn.y);
        int topY = randomNumber(r, (int) (notSpawn.y + notSpawn.getHeight()), (int) (spawn.y + spawn.getHeight()));

        int x = r.nextBoolean() ? LeftX : RightX;
        int y = r.nextBoolean() ? bottomY : topY;

        return new ArrayList<Integer>(Arrays.asList(x, y));
    }

    private int randomNumber(Random random, int min, int max) {
        return random.nextInt(max - min) + min;
    }


}
