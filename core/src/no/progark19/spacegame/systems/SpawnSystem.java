package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import no.progark19.spacegame.utils.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.SweepComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.utils.ComponentMappers;
import no.progark19.spacegame.utils.EntityFactory;
import no.progark19.spacegame.utils.Paths;

public class SpawnSystem extends EntitySystem {

    private EntityFactory entityFactory;
    private SpaceGame game;
    boolean fire = true;

    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> bullets;
    private int numAsteroids = 0;

    private Rectangle spawn = new Rectangle();
    private Rectangle notSpawn = new Rectangle();
    boolean yeah = true;

    //TODO BOUNDING BOX TO kvadrat.....
    public SpawnSystem(SpaceGame game, EntityFactory entityFactory) {
        this.game = game;
        this.entityFactory = entityFactory;
        //------------Testing projectile-----------------
        //engine.addEntity(entityFactory.createAsteroid(game.camera .position.x + 100 ,game. camera.position.y,new Vector2(0, 0), world, false));
        //engine.addEntity(entityFactory.createAsteroid(game.camera.position.x - 200 ,game.camera.position.y -100,new Vector2(0, 0), world, false));
        //-----------------------------------------------
    }


    public void addedToEngine(Engine engine) {
        asteroids = engine.getEntitiesFor(Family.all(BodyComponent.class, SpriteComponent.class, HealthComponent.class, ElementComponent.class).get());
        bullets = engine.getEntitiesFor(Family.all(
                ElementComponent.class,
                SpriteComponent.class,
                VelocityComponent.class).get());
    }


    public void update(float deltaTime) {
        if (bullets.size() == 0) {
            Entity entity = entityFactory.createProjectile(game.camera.position.x, game.camera.position.y, -500,0, GameSettings.BULLET_TYPE);
            getEngine().addEntity(entity);
            Sound sound = game.assetManager.get(Paths.SOUND_SHOT_FIRED);
            sound.play(0.1f * GameSettings.EFFECTS_VOLUME);
        }

        GameSettings.screenBounds.set((int) game.camera.position.x - (SpaceGame.WIDTH), (int) game.camera.position.y - (SpaceGame.WIDTH), SpaceGame.WIDTH * 2, (SpaceGame.WIDTH * 2));
        for (Entity entity : bullets) {
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            if (!GameSettings.screenBounds.contains(scom.sprite.getX(), scom.sprite.getY())) {
                entity.add(new SweepComponent());
            }
        }

        if (yeah) {
            //getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x - 200 ,game.camera.position.y + 300 ,new Vector2(1, -1), "ICE"));
            //getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x ,game.camera.position.y + 100 ,new Vector2(0, 0), "FIRE"));

            getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x - 200 ,game.camera.position.y-20 ,new Vector2(0, 0), "ICE"));
            //getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x - 500 ,game.camera.position.y-50 ,new Vector2(0, 0), "ICE"));
            //getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x - 800 ,game.camera.position.y-50 ,new Vector2(0, 0), "FIRE"));
            //getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x ,game.camera.position.y+600 ,new Vector2(0, -1), "ICE"));
            //getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x ,game.camera.position.y-600 ,new Vector2(0, +1), "FIRE"));
            yeah = false;
            //getEngine().addEntity(entityFactory.createAsteroid(game.camera.position.x - 300 ,game.camera.position.y-300 ,new Vector2(1, 1), "ICE"));


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
                GameSettings.BOX2D_PHYSICSWORLD.destroyBody(bcom.body);
                entity.add(new SweepComponent());
                numAsteroids--;

            }
        }
        /*
        GameSettings.screenBounds.set((int) game.camera.position.x - (SpaceGame.WIDTH), (int) game.camera.position.y - (SpaceGame.WIDTH), SpaceGame.WIDTH * 2, (SpaceGame.WIDTH * 2));
        for (Entity entity : bullets) {
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            if (!GameSettings.screenBounds.contains(scom.sprite.getX(), scom.sprite.getY())) {
                entity.add(new SweepComponent());
            }
        }
        */
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
