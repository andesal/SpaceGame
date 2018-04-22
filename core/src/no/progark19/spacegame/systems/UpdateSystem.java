package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;

import no.progark19.spacegame.utils.GameSettings;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.components.DamagedComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.FuelBarComponent;
import no.progark19.spacegame.components.FuelComponent;
import no.progark19.spacegame.components.FuelUsageComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.HealthbarComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.RewardComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.managers.EntityManager;
import no.progark19.spacegame.utils.ComponentMappers;
import no.progark19.spacegame.utils.EntityFactory;
import no.progark19.spacegame.utils.MyProgressBar;
import no.progark19.spacegame.utils.Paths;

public class UpdateSystem extends EntitySystem {

    private SpaceGame game;
    private EntityFactory entityFactory;

    private ImmutableArray<Entity> damaged;
    private ImmutableArray<Entity> rewarded;
    private ImmutableArray<Entity> spaceship;


    public UpdateSystem(SpaceGame game, EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
        this.game = game;
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

            HealthbarComponent hbcom = ComponentMappers.HEALTHBAR_MAP.get(entity);
            FuelBarComponent fbcom = ComponentMappers.FUELBAR_MAP.get(entity);


            SpriteComponent scom = ComponentMappers.SPRITE_MAP.get(entity);
            ElementComponent ecom = ComponentMappers.ELEMENT_MAP.get(entity);
            hcom.health -= dcom.damage;

            if (hcom.health <= 0 && fbcom == null) {
                EntityManager.flaggedForRemoval.add(entity);
                float x = scom.sprite.getX() + scom.sprite.getOriginX();
                float y = scom.sprite.getY() + scom.sprite.getOriginY();
                if ("FIRE".equals(ecom.element)) {
                    getEngine().addEntity(entityFactory.createPowerup(x, y, EntityFactory.POWERUPS.HEALTH));
                } else {
                    getEngine().addEntity(entityFactory.createPowerup(x, y, EntityFactory.POWERUPS.FUEL));
                }
                Sound sound = game.assetManager.get(Paths.SOUND_ASTEROID_EXPLOSION, Sound.class);
                sound.play(0.1f * GameSettings.EFFECTS_VOLUME);
            } else {
                if (hcom != null) {
                    //hbcom.bar.setValue((float) hcom.health/100);
                    if (hcom.health <= 0) {
                        System.out.println("GAME OVER HEALTH");
                        GameSettings.GAME_STATE = 2;
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
                }
            } else {
                FuelComponent fcom = ComponentMappers.FUEL_MAP.get(entity);
                if (fcom.fuel + rcom.reward <= GameSettings.MAX_FUEL) {
                    fcom.fuel += rcom.reward;
                }
            }
            Sound sound = game.assetManager.get(Paths.SOUND_POWERUP, Sound.class);
            sound.play(0.2f * GameSettings.EFFECTS_VOLUME);
            entity.remove(RewardComponent.class);
        }

        for (Entity entity : spaceship) {
            FuelComponent fcom = ComponentMappers.FUEL_MAP.get(entity);
            FuelUsageComponent ucom = ComponentMappers.FUEL_USAGE_MAP.get(entity);
            fcom.fuel -= ucom.usage;
            entity.remove(FuelUsageComponent.class);
            if (fcom.fuel <= 0) {
                //System.out.println("GAME OVER FUEL"); Not implemented
            } else {
            }
            entity.remove(FuelUsageComponent.class);
        }

    }

}
