package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.HashMap;

import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SynchronizedComponent;
import no.progark19.spacegame.interfaces.P2pConnector;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.json.JsonPayload;
import no.progark19.spacegame.utils.json.JsonPayloadTags;

/**
 * Created by Anders on 19.04.2018.
 */

public class NetworkSystem extends IntervalSystem {
    private JsonPayload payload;
    private P2pConnector connector;
    private HashMap<String, Object> payloadMap;


    private ImmutableArray<Entity> synchronizedPhysicsObjects;

    public NetworkSystem(float interval, P2pConnector connector) {
        super(interval);
        this.connector = connector;
    }

    @Override
    public void addedToEngine(Engine engine) {
        synchronizedPhysicsObjects = engine.getEntitiesFor(Family
                .all(
                        RenderableComponent.class,
                        PositionComponent.class,
                        SynchronizedComponent.class)
                .get());
    }

    @Override
    protected void updateInterval() {
        System.out.println("SYNCING");
        for (Entity e: synchronizedPhysicsObjects) {
            PositionComponent pcom = ComponentMappers.POS_MAP.get(e);

            payload = new JsonPayload();
            payload.setTAG(JsonPayloadTags.SYNC_BODY);
            payloadMap = new HashMap<String, Object>();
            payloadMap.put(JsonPayloadTags.SYNC_ENTITYID, EntityManager.getEntityID(e));
            payloadMap.put(JsonPayloadTags.SYNC_ROTATION, pcom.rotation);
            payloadMap.put(JsonPayloadTags.SYNC_POSX, pcom.x);
            payloadMap.put(JsonPayloadTags.SYNC_POSY, pcom.y);
            payload.setValue(payloadMap);

            connector.sendData(payload);
        }
    }
}
