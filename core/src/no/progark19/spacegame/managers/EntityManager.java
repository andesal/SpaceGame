package no.progark19.spacegame.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashMap;
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
import no.progark19.spacegame.utils.Assets;
import no.progark19.spacegame.utils.EntityFactory;


public class EntityManager implements EntityListener{
    private int entityID = 0;

    private static HashMap<Integer, Entity> integerEntityMap = new HashMap<Integer, Entity>();
    private static HashMap<Entity, Integer> entityIntegerMap = new HashMap<Entity, Integer>();

    public EntityManager() {
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
