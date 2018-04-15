package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.progark19.spacegame.components.BodyComponent;
import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.ForceApplierComponent;
import no.progark19.spacegame.components.GravityComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.ParentComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RelativePositionComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.SoundComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.VelocityComponent;

/**
 * Created by anderssalvesen on 10.04.2018.
 */

public class ComponentMappers {

    public static final ComponentMapper<BodyComponent> BOD_MAP = ComponentMapper.getFor(BodyComponent.class);

    public static final ComponentMapper<ElementComponent> ELEMENT_MAP = ComponentMapper.getFor(ElementComponent.class);

    public static final ComponentMapper<GravityComponent> GRAV_MAP = ComponentMapper.getFor(GravityComponent.class);

    public static final ComponentMapper<HealthComponent> HEALTH_MAP = ComponentMapper.getFor(HealthComponent.class);

    public static final ComponentMapper<ParentComponent> PARENT_MAP = ComponentMapper.getFor(ParentComponent.class);

    public static final ComponentMapper<PositionComponent> POS_MAP = ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<PowerupComponent> POWER_MAP = ComponentMapper.getFor(PowerupComponent.class);

    public static final ComponentMapper<RelativePositionComponent> RELPOS_MAP = ComponentMapper.getFor(RelativePositionComponent.class);

    public static final ComponentMapper<RenderableComponent> RENDER_MAP = ComponentMapper.getFor(RenderableComponent.class);

    public static final ComponentMapper<RotationComponent> ROT_MAP = ComponentMapper.getFor(RotationComponent.class);

    public static final ComponentMapper<SoundComponent> SOUND_MAP = ComponentMapper.getFor(SoundComponent.class);

    public static final ComponentMapper<SpriteComponent> SPRITE_MAP = ComponentMapper.getFor(SpriteComponent.class);

    public static final ComponentMapper<VelocityComponent> VEL_MAP = ComponentMapper.getFor(VelocityComponent.class);

    public static final ComponentMapper<ForceApplierComponent> FORCE_MAP = ComponentMapper.getFor(ForceApplierComponent.class);

}
