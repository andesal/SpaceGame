package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.SweepComponent;
import no.progark19.spacegame.utils.EntityFactory;

public class SpawnSystem extends EntitySystem {

    private EntityFactory entityFactory;
    private World world;
    private SpaceGame game;
    boolean fire = true;

    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> obstacles;
    private ImmutableArray<Entity> collectables;
    private int numAsteroids = 30;

    private Rectangle spawn = new Rectangle();
    private Rectangle notSpawn = new Rectangle();
    boolean yeah = true;

    //TODO BOUNDING BOX TO kvadrat.....
    public SpawnSystem(SpaceGame game, World world, EntityFactory entityFactory) {
        this.game = game;
        this.entityFactory = entityFactory;
        this.world = world;
        //------------Testing projectile-----------------
        //engine.addEntity(entityFactory.createAsteroid(game.camera .position.x + 100 ,game. camera.position.y,new Vector2(0, 0), world, false));
        //engine.addEntity(entityFactory.createAsteroid(game.camera.position.x - 200 ,game.camera.position.y -100,new Vector2(0, 0), world, false));
        //-----------------------------------------------
    }


    public void addedToEngine(Engine engine) {
        asteroids = engine.getEntitiesFor(Family.all(BodyComponent.class, SpriteComponent.class, HealthComponent.class, ElementComponent.class).get());
    }

    public void update(float deltaTime) {
        if (yeah) {

            getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x - 200 ,game.camera.position.y-20 ,new Vector2(0, 0), "FIRE"));
            //getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x - 500 ,game.camera.position.y-50 ,new Vector2(0, 0), "FIRE"));
            getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x - 800 ,game.camera.position.y-50 ,new Vector2(0, 0), "FIRE"));
            //getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x ,game.camera.position.y+300 ,new Vector2(0, -1), "FIRE"));
            getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x ,game.camera.position.y-300 ,new Vector2(0, +1), "FIRE"));
            yeah = false;


        }
        notSpawn = new Rectangle(game.camera.position.x - 720, game.camera.position.y - 1080, 1440, 2160);
        spawn = new Rectangle(notSpawn.x - 480, notSpawn.y - 720, 2400, 3600);
        //TODO This while on game start (IF game.started (boolean in settings) . If new asteroids is needed, re-use entity (Remove & add renderable component and re-calculate coordinates, reset health etc)
        //TODO if asteroid explodes (collision system), update pos, health etc .
        while (numAsteroids < GameSettings.MAX_ASTEROIDS) {
            int x = calculateSpawnCoordinates().get(0);
            int y = calculateSpawnCoordinates().get(1);
            float velX = randomNumber(-200,200)/ GameSettings.BOX2D_PIXELS_TO_METERS;
            float velY = randomNumber(-200,200)/ GameSettings.BOX2D_PIXELS_TO_METERS;
            fire = !fire;
            String element = fire ? "FIRE" : "ICE";
            Entity entity = entityFactory.createAsteroid(x, y, new Vector2(velX, velY), element);
            getEngine().addEntity(entity);
            numAsteroids++;

        }

        for (Entity entity : asteroids) {
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            BodyComponent bcom = ComponentMappers.BOD_MAP.get(entity);
            float x = scom.sprite.getX() + scom.sprite.getOriginX();
            float y = scom.sprite.getY() + scom.sprite.getOriginY();
            if (GameSettings.screenBounds.contains(x, y)) {
                entity.add(new RenderableComponent());
            }
            if (! spawn.contains(x, y)) {
                world.destroyBody(bcom.body);
                entity.add(new SweepComponent());
                numAsteroids--;

            }
        }
    }

    private ArrayList<Integer> calculateSpawnCoordinates() {
        Random r = GameSettings.getMainRandom();
        int LeftX = randomNumber((int) spawn.x, (int) notSpawn.x);
        int RightX = randomNumber((int) (notSpawn.x + notSpawn.getWidth()), (int) (spawn.x + spawn.getWidth()));
        int bottomY = randomNumber((int) spawn.y, (int) notSpawn.y);
        int topY = randomNumber((int) (notSpawn.y + notSpawn.getHeight()), (int) (spawn.y + spawn.getHeight()));

        int x = r.nextBoolean() ? LeftX : RightX;
        int y = r.nextBoolean() ? bottomY : topY;

        return new ArrayList<Integer>(Arrays.asList(x, y));
    }

    private int randomNumber(int min, int max) {
        return GameSettings.getMainRandom().nextInt(max - min) + min;
    }


}
