package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.ForceOnComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.managers.EntityManager;

/**
 * Created by Anders on 15.04.2018.
 */

public class ForceApplierSystem extends EntitySystem{
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family
                .all(
                        ForceApplierComponent.class,
                        ForceOnComponent.class,
                        ParentComponent.class)
                .get()
        );
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity: entities) {
            ForceApplierComponent fcom = ComponentMappers.FORCE_MAP.get(entity);
            ParentComponent parcom = ComponentMappers.PARENT_MAP.get(entity);
            Entity parent = EntityManager.getEntity(parcom.parentID);
            BodyComponent bcom_parent = ComponentMappers.BOD_MAP.get(parent);

            System.out.println(fcom);

            Vector2 forceVector = new Vector2(fcom.force, 0);
            forceVector.setAngle(fcom.direction);
            bcom_parent.body.applyForceToCenter(forceVector, true);
        }
    }
}
