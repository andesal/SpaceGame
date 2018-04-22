package no.progark19.spacegame.utils;

import com.badlogic.ashley.core.ComponentMapper;

import no.progark19.spacegame.components.AnimationComponent;
import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.DamagedComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.FuelBarComponent;
import no.progark19.spacegame.components.FuelComponent;
import no.progark19.spacegame.components.FuelUsageComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.HealthbarComponent;
import no.progark19.spacegame.components.LeadCameraComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.RewardComponent;
import no.progark19.spacegame.components.SoundComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.SweepComponent;
import no.progark19.spacegame.components.VelocityComponent;

/**
 * Created by anderssalvesen on 10.04.2018.
 */

public class ComponentMappers {
    public static final ComponentMapper<HealthbarComponent> HEALTHBAR_MAP = ComponentMapper.getFor(HealthbarComponent.class);

    public static final ComponentMapper<FuelBarComponent> FUELBAR_MAP = ComponentMapper.getFor(FuelBarComponent.class);

    public static final ComponentMapper<AnimationComponent> ANI_MAP = ComponentMapper.getFor(AnimationComponent.class);

    public static final ComponentMapper<BodyComponent> BOD_MAP = ComponentMapper.getFor(BodyComponent.class);

    public static final ComponentMapper<DamagedComponent> DAM_MAP = ComponentMapper.getFor(DamagedComponent.class);

    public static final ComponentMapper<ElementComponent> ELEMENT_MAP = ComponentMapper.getFor(ElementComponent.class);

    public static final ComponentMapper<ForceApplierComponent> FORCE_MAP = ComponentMapper.getFor(ForceApplierComponent.class);

    public static final ComponentMapper<FuelComponent> FUEL_MAP = ComponentMapper.getFor(FuelComponent.class);

    public static final ComponentMapper<FuelUsageComponent> FUEL_USAGE_MAP = ComponentMapper.getFor(FuelUsageComponent.class);

    public static final ComponentMapper<HealthComponent> HEALTH_MAP = ComponentMapper.getFor(HealthComponent.class);

    public static final ComponentMapper<LeadCameraComponent> LEAD_MAP = ComponentMapper.getFor(LeadCameraComponent.class);

    public static final ComponentMapper<ParentComponent> PARENT_MAP = ComponentMapper.getFor(ParentComponent.class);

    public static final ComponentMapper<PositionComponent> POS_MAP = ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<PowerupComponent> POWER_MAP = ComponentMapper.getFor(PowerupComponent.class);

    public static final ComponentMapper<RelativePositionComponent> RELPOS_MAP = ComponentMapper.getFor(RelativePositionComponent.class);

    public static final ComponentMapper<RewardComponent> REW_COMP = ComponentMapper.getFor(RewardComponent.class);

    public static final ComponentMapper<RenderableComponent> RENDER_MAP = ComponentMapper.getFor(RenderableComponent.class);

    public static final ComponentMapper<SoundComponent> SOUND_MAP = ComponentMapper.getFor(SoundComponent.class);

    public static final ComponentMapper<SpriteComponent> SPRITE_MAP = ComponentMapper.getFor(SpriteComponent.class);

    public static final ComponentMapper<VelocityComponent> VEL_MAP = ComponentMapper.getFor(VelocityComponent.class);

}
