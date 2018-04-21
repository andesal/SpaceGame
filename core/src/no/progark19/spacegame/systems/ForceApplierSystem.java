package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.ForceOnComponent;
import no.progark19.spacegame.components.FuelUsageComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.Paths;

/**
 * Created by Anders on 15.04.2018.
 */

public class ForceApplierSystem extends EntitySystem{

    private ImmutableArray<Entity> entities;
    private Sprite controllDebugForceArrow;
    private SpaceGame game;

    public ForceApplierSystem(SpaceGame game) {
        this.game = game;
        this.controllDebugForceArrow  = new Sprite(game.assetManager.get(Paths.DEBUG_FORCEARROW_TEXTURE_PATH, Texture.class));
        this.controllDebugForceArrow.setOrigin(GameSettings.DEBUG_FORCEARROW_ORIGIN.x, GameSettings.DEBUG_FORCEARROW_ORIGIN.y);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family
                .all(
                        ForceApplierComponent.class,
                        ForceOnComponent.class,
                        ParentComponent.class,
                        RelativePositionComponent.class)
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
            PositionComponent parposcom = ComponentMappers.POS_MAP.get(parent);
            RelativePositionComponent relcom = ComponentMappers.RELPOS_MAP.get(entity);
            FuelUsageComponent ucom = ComponentMappers.FUEL_USAGE_MAP.get(entity);

            //Update fuel consumption
            if (ucom == null) {
                parent.add(new FuelUsageComponent(deltaTime));

            } else {
                ucom.usage += deltaTime;
            }

            Vector2 forceVector = new Vector2(fcom.force, 0);
            forceVector.setAngle(fcom.direction + parposcom.rotation);

            Vector2 relPos = new Vector2(relcom.x, relcom.y);
            Vector2 newRelPos = relPos.cpy().setAngle(relPos.angle() + parposcom.rotation);

            float relX = parposcom.x + newRelPos.x;
            float relY = parposcom.y + newRelPos.y;

            //FIXME Remove; only for debug
            controllDebugForceArrow.setOriginBasedPosition(relX, relY);
            controllDebugForceArrow.setRotation(forceVector.angle());
            RenderSystem.forceDraw(controllDebugForceArrow);
            //------------------------------------------------

            //bcom_parent.body.applyForceToCenter(forceVector, true);
            if(GameSettings.SPACESHIP_ENABLE_ROTATION){
                System.out.println(bcom_parent);
                bcom_parent.body.applyForce(
                        forceVector,
                        (new Vector2(relX, relY)).scl(1f/ GameSettings.BOX2D_PIXELS_TO_METERS),
                        true);
            } else {
                bcom_parent.body.applyForceToCenter(
                        forceVector,
                        true);
            }

        }
    }
}
