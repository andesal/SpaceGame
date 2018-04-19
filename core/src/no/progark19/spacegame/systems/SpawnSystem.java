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
    private int numAsteroids = 0;

    private Rectangle spawn = new Rectangle();
    private Rectangle notSpawn = new Rectangle();
    boolean yeah = true;
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
        //------------Testing projectile-----------------

        //engine.addEntity(entityFactory.createAsteroid(camera.position.x + 100 ,camera.position.y + 300 ,new Vector2(0, -1), world, true));
        //engine.addEntity(entityFactory.createAsteroid(camera.position.x + 100 ,camera.position.y,new Vector2(0, 0), world, true));
        //engine.addEntity(entityFactory.createAsteroid(camera.position.x - 200 ,camera.position.y -100,new Vector2(0, 0), world, true));


        //-----------------------------------------------
    }


    public void addedToEngine(Engine engine) {
        asteroids = engine.getEntitiesFor(Family.all(BodyComponent.class, SpriteComponent.class, HealthComponent.class, ElementComponent.class).get());
        obstacles = engine.getEntitiesFor(Family.all(PositionComponent.class, GravityComponent.class,
                RotationComponent.class).get());
        collectables = engine.getEntitiesFor(Family.all(PositionComponent.class,
                PowerupComponent.class).get());


    }

    public void update(float deltaTime) {
        if (yeah) {
            engine.addEntity(entityFactory.createAsteroid(camera.position.x - 200 ,camera.position.y +100,new Vector2(0, 0), world, true));
            System.out.println("xx: " + (camera.position.x -200) + " : " + "yy: " + (camera.position.y +100));

            yeah = false;
        }
        Random r = new Random();
        notSpawn = new Rectangle(camera.position.x - 720, camera.position.y - 1080, 1440, 2160);
        spawn = new Rectangle(notSpawn.x - 480, notSpawn.y - 720, 2400, 3600);

        //TODO This while on game start (IF game.started (boolean in settings). If new asteroids is needed, re-use entity (Remove & add renderable component and re-calculate coordinates, reset health etc)
        //TODO if asteroid explodes (collision system), update pos, health etc.
        while (numAsteroids < GameSettings.MAX_ASTEROIDS) {
            int x = calculateSpawnCoordinates(r).get(0);
            int y = calculateSpawnCoordinates(r).get(1);
            float velX = randomNumber(r, -200,200)/ GameSettings.BOX2D_PIXELS_TO_METERS;
            float velY = randomNumber(r, -200,200)/ GameSettings.BOX2D_PIXELS_TO_METERS;
            fire = !fire;
            engine.addEntity(entityFactory.createAsteroid(x,y,new Vector2(velX, velY), world, fire));
            numAsteroids++;

        }

        for (Entity entity : asteroids) {
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            BodyComponent bcom = ComponentMappers.BOD_MAP.get(entity);
            float x = scom.sprite.getX() + scom.sprite.getOriginX();
            float y = scom.sprite.getY() + scom.sprite.getOriginY();
            if (! spawn.contains(x, y)) {
                world.destroyBody(bcom.body);
                engine.removeEntity(entity);
                numAsteroids--;

            }
        }
    }


    private ArrayList<Integer> calculateSpawnCoordinates() {
        int LeftX = randomNumber((int) spawn.x, (int) notSpawn.x);
        int RightX = randomNumber((int) (notSpawn.x + notSpawn.getWidth()), (int) (spawn.x + spawn.getWidth()));
        int bottomY = randomNumber((int) spawn.y, (int) notSpawn.y);
        int topY = randomNumber((int) (notSpawn.y + notSpawn.getHeight()), (int) (spawn.y + spawn.getHeight()));

        int x = GameSettings.getMainRandom().nextBoolean() ? LeftX : RightX;
        int y = GameSettings.getMainRandom().nextBoolean() ? bottomY : topY;

        return new ArrayList<Integer>(Arrays.asList(x, y));
    }

    private int randomNumber(int min, int max) {
        return GameSettings.getMainRandom().nextInt(max - min) + min;
    }


}
