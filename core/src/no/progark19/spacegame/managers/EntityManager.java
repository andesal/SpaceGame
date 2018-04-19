package no.progark19.spacegame.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;

import java.util.HashMap;


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
        //System.out.println("Entity added");
    }

    @Override
    public void entityRemoved(Entity entity) {
        integerEntityMap.remove(entityIntegerMap.remove(entity));
        //System.out.println("Entity removed");
    }

    public static Entity getEntity(int id){
        return integerEntityMap.get(id);
    }

    public static int getEntityID(Entity entity){
        return entityIntegerMap.get(entity);
    }

}
