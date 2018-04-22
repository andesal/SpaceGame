package no.progark19.spacegame.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.HashMap;

import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.utils.ComponentMappers;
import no.progark19.spacegame.utils.EntityFactory;


public class EntityManager implements EntityListener{

    private PooledEngine engine;
    private EntityFactory entityFactory;
    public static ArrayList<Entity> flaggedForRemoval = new ArrayList<Entity>();

    private int entityID = 0;

    private static HashMap<Integer, Entity> integerEntityMap = new HashMap<Integer, Entity>();
    private static HashMap<Entity, Integer> entityIntegerMap = new HashMap<Entity, Integer>();

    public EntityManager(PooledEngine engine, EntityFactory entityFactory) {
        this.engine = engine;
        this.entityFactory = entityFactory;
    }

    @Override
    public void entityAdded(Entity entity) {
        //For collision handling
        if (ComponentMappers.BOD_MAP.get(entity) != null) {
            BodyComponent bcom = ComponentMappers.BOD_MAP.get(entity);
            bcom.body.setUserData(entity);
        }
        integerEntityMap.put(entityID, entity);
        entityIntegerMap.put(entity, entityID);
        entityID ++;
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (ComponentMappers.BOD_MAP.get(entity) != null && ComponentMappers.LEAD_MAP.get(entity) == null) {
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            ElementComponent ecom = ComponentMappers.ELEMENT_MAP.get(entity);
            float x = scom.sprite.getX() + scom.sprite.getOriginX();
            float y = scom.sprite.getY() + scom.sprite.getOriginY();
            Entity explosionEntity = entityFactory.createAnimationEntity(x, y, ecom.element);
            engine.addEntity(explosionEntity);
        }
        integerEntityMap.remove(entityIntegerMap.remove(entity));
    }

    public static Entity getEntity(int id){
        return integerEntityMap.get(id);
    }

    public static int getEntityID(Entity entity){
        return entityIntegerMap.get(entity);
    }

}
