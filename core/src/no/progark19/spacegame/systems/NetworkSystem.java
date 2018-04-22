package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.Arrays;
import java.util.HashMap;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SynchronizedComponent;
import no.progark19.spacegame.interfaces.P2pConnector;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.RenderableWorldState;
import no.progark19.spacegame.utils.json.JsonPayload;
import no.progark19.spacegame.utils.json.WorldStateIndexes;

/**
 * Created by Anders on 19.04.2018.
 */

public class NetworkSystem extends IntervalSystem {
    private JsonPayload payload;
    private P2pConnector connector;
    private HashMap<String, Object> payloadMap;


    private ImmutableArray<Entity> synchronizedPhysicsObjects;
    //private ImmutableArray<Enti>

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
        //System.out.println("SYNCING");
        RenderableWorldState rws = new RenderableWorldState(synchronizedPhysicsObjects.size());
        for (Entity e: synchronizedPhysicsObjects) {
            /*PositionComponent pcom = ComponentMappers.POS_MAP.get(e);
            BodyComponent     bcom = ComponentMappers.BOD_MAP.get(e);

            float[] state = new float[WorldStateIndexes.WS_NUMBER_OF_VALUES];
            state[WorldStateIndexes.WS_ENTITYID] = EntityManager.getEntityID(e);
            state[WorldStateIndexes.WS_RENDERABLE_POSX] = pcom.x;
            state[WorldStateIndexes.WS_RENDERABLE_POSY] = pcom.y;
            state[WorldStateIndexes.WS_RENDERABLE_ROTATION] = pcom.rotation;

            state[WorldStateIndexes.WS_RENDERABLE_VX] = bcom.body.getLinearVelocity().x * GameSettings.BOX2D_PIXELS_TO_METERS;
            state[WorldStateIndexes.WS_RENDERABLE_VY] = bcom.body.getLinearVelocity().y * GameSettings.BOX2D_PIXELS_TO_METERS;
            state[WorldStateIndexes.WS_RENDERABLE_VR] = bcom.body.getAngularVelocity()  * GameSettings.BOX2D_PIXELS_TO_METERS;

            rws.addState(state);
            //System.out.println("Made state: " + Arrays.toString(state));

            /*payload = new JsonPayload();
            payload.setTAG(JsonPayloadTags.SYNC_BODY);
            payloadMap = new HashMap<String, Object>();
            payloadMap.put(, EntityManager.getEntityID(e));
            payloadMap.put(JsonPayloadTags.SYNC_ROTATION, pcom.rotation);
            payloadMap.put(JsonPayloadTags.SYNC_POSX, pcom.x);
            payloadMap.put(JsonPayloadTags.SYNC_POSY, pcom.y);
            payload.setValue(payloadMap);*/

        }
        //connector.sendData(rws);
    }
}
