package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.DamagedComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.FuelComponent;
import no.progark19.spacegame.components.FuelUsageComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.RewardComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.EntityFactory;
import no.progark19.spacegame.utils.MyProgressBar;

public class UpdateSystem extends EntitySystem {

    private EntityFactory entityFactory;
    private MyProgressBar healthBar;
    private MyProgressBar fuelBar;

    private ImmutableArray<Entity> damaged;
    private ImmutableArray<Entity> rewarded;
    private ImmutableArray<Entity> spaceship;


    public UpdateSystem(EntityFactory entityFactory, MyProgressBar healthBar, MyProgressBar fuelBar) {
        this.entityFactory = entityFactory;
        this.healthBar = healthBar;
        this.fuelBar = fuelBar;
    }

    public void addedToEngine(Engine engine) {
        damaged = engine.getEntitiesFor(Family.all(DamagedComponent.class, HealthComponent.class).get());
        rewarded = engine.getEntitiesFor(Family.all(RewardComponent.class).get());
        spaceship = engine.getEntitiesFor(Family.all(FuelUsageComponent.class).get());
    }

    public void update(float deltatTime) {
        for (Entity entity : damaged) {
            HealthComponent hcom = ComponentMappers.HEALTH_MAP.get(entity);
            DamagedComponent dcom = ComponentMappers.DAM_MAP.get(entity);
            LeadCameraComponent lcom = ComponentMappers.LEAD_MAP.get(entity); //Used to check if entity is spaceship...
            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            ElementComponent ecom = ComponentMappers.ELEMENT_MAP.get(entity);
            hcom.health -= dcom.damage;

            if (hcom.health <= 0 && lcom == null) {
                EntityManager.flaggedForRemoval.add(entity);
                float x = scom.sprite.getX() + scom.sprite.getOriginX();
                float y = scom.sprite.getY() + scom.sprite.getOriginY();
                if (ecom.element == EntityFactory.ELEMENTS.FIRE) {
                    getEngine().addEntity(entityFactory.createPowerup(x, y, EntityFactory.POWERUPS.HEALTH));
                } else {
                    getEngine().addEntity(entityFactory.createPowerup(x, y, EntityFactory.POWERUPS.FUEL));
                }
            } else {
                if (lcom != null) {
                    healthBar.setValue((float) hcom.health/100);
                    if (hcom.health <= 0) {
                        System.out.println("GAME OVER HEALTH");
                        GameSettings.IS_GAME_OVER = true;
                    }
                }
                entity.remove(DamagedComponent.class);
            }
        }

        for (Entity entity : rewarded) {
            RewardComponent rcom = ComponentMappers.REW_COMP.get(entity);
            if (rcom.type.equals("health")) {
                HealthComponent hcom = ComponentMappers.HEALTH_MAP.get(entity);
                if (hcom.health + rcom.reward <= GameSettings.MAX_HEALTH_SPACESHIP) {
                    hcom.health += rcom.reward;
                    healthBar.setValue((float) hcom.health/100);
                }
            } else {
                FuelComponent fcom = ComponentMappers.FUEL_MAP.get(entity);
                if (fcom.fuel + rcom.reward <= GameSettings.MAX_FUEL) {
                    fcom.fuel += rcom.reward;
                    fuelBar.setValue((ComponentMappers.FUEL_MAP.get(entity).fuel + rcom.reward)/100);
                }
            }
            entity.remove(RewardComponent.class);
        }

        for (Entity entity : spaceship) {
            System.out.println("HEALTH = " + ComponentMappers.HEALTH_MAP.get(entity).health+ " : " + ComponentMappers.FUEL_MAP.get(entity).fuel + " = " + "FUEL");
            FuelComponent fcom = ComponentMappers.FUEL_MAP.get(entity);
            FuelUsageComponent ucom = ComponentMappers.FUEL_USAGE_MAP.get(entity);
            fcom.fuel -= ucom.usage;
            entity.remove(FuelUsageComponent.class);
            if (fcom.fuel <= 0) {
                System.out.println("NO FUEL LEFT");
            } else {
                fuelBar.setValue(fcom.fuel/100);

            }
            entity.remove(FuelUsageComponent.class);
        }

    }

}
