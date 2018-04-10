package no.progark19.spacegame.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import javax.sound.sampled.SourceDataLine;

import no.progark19.spacegame.components.PositionComponent;
import no.progark19.spacegame.components.PowerupComponent;
import no.progark19.spacegame.components.RenderableComponent;
import no.progark19.spacegame.components.SoundComponent;
import no.progark19.spacegame.managers.AudioManager;


public class SoundSystem extends EntitySystem implements EntityListener {

    private ImmutableArray<Entity> entities;
    private Sound theme;
    private AudioManager audioManager;

    public SoundSystem() {
        audioManager = new AudioManager();
    }

    public SoundSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SoundComponent.class, RenderableComponent.class, PositionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        audioManager.update();
        for (Entity entity : entities) {
            PositionComponent pcom = ComponentMappers.pm.get(entity);
            if (pcom.x > 4000) {
                audioManager.playSound(audioManager.explosion1, 1, 1, 1,false);
            }

        }
    }


    @Override
    public void entityAdded(Entity entity) {

    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
