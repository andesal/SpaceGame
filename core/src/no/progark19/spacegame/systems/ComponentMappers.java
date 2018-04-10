package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.progark19.spacegame.components.ElementComponent;
import no.progark19.spacegame.components.GravityComponent;
import no.progark19.spacegame.components.HealthComponent;
import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.RotationComponent;
import no.progark19.spacegame.components.SoundComponent;
import no.progark19.spacegame.components.SpriteComponent;
import no.progark19.spacegame.components.VelocityComponent;

/**
 * Created by anderssalvesen on 10.04.2018.
 */

public class ComponentMappers {

    public static final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<HealthComponent> hm = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<ElementComponent> em = ComponentMapper.getFor(ElementComponent.class);
    public static final ComponentMapper<RotationComponent> rm = ComponentMapper.getFor(RotationComponent.class);
    public static final ComponentMapper<GravityComponent> gm = ComponentMapper.getFor(GravityComponent.class);
    public static final ComponentMapper<PowerupComponent> pom = ComponentMapper.getFor(PowerupComponent.class);
    public static final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<SoundComponent> som = ComponentMapper.getFor(SoundComponent.class);
    public static final ComponentMapper<RenderableComponent> rem = ComponentMapper.getFor(RenderableComponent.class);


}
