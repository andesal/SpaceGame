package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;


import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.SweepComponent;
import no.progark19.spacegame.components.VelocityComponent;
import no.progark19.spacegame.utils.EntityFactory;
import no.progark19.spacegame.utils.Paths;

public class ControlSystem extends EntitySystem {

    private ImmutableArray<Entity> bullets;
    private ImmutableArray<Entity> asteroids;

    boolean testOneBullet = true;
    public float temp;

    private SpaceGame game;
    private EntityFactory entityFactory;

    public ControlSystem(SpaceGame game, EntityFactory entityFactory) {
        this.game = game;
        this.entityFactory = entityFactory;
    }

    public void addedToEngine(Engine engine) {
        bullets = engine.getEntitiesFor(Family.all(
                ElementComponent.class,
                SpriteComponent.class,
                VelocityComponent.class).get());
        }
    //TODO remove asteroid entity if dead
    public void update(float deltaTime) {
        //TODO REMOVE FLIP
        if (bullets.size() == 0) {
            Entity entity = entityFactory.createProjectile(game.camera.position.x, game.camera.position.y, new Vector2(-5,0), GameSettings.BULLET_TYPE);
            getEngine().addEntity(entity);
            Sound sound = game.assetManager.get(Paths.SOUND_SHOT_FIRED);
            sound.play(0.1f * GameSettings.EFFECTS_VOLUME);
        }
        GameSettings.screenBounds.set((int) game.camera.position.x - (SpaceGame.WIDTH), (int) game.camera.position.y - (SpaceGame.WIDTH), SpaceGame.WIDTH * 2, (SpaceGame.WIDTH * 2));
        temp += deltaTime;
        if (temp > 1) {
            for (Entity entity : bullets) {
                SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
                if (!GameSettings.screenBounds.contains(scom.sprite.getX(), scom.sprite.getY())) {
                    entity.add(new SweepComponent());
                }
            }
        }
    }


}
