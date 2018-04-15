package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Anders on 14.04.2018.
 */

public class RelativePositionComponent implements Component, Pool.Poolable {
    public float x = 0.0f;
    public float y = 0.0f;
    public float rotation = 0;

    public RelativePositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
    }

    public RelativePositionComponent(float x, float y, float rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    @Override
    public void reset() {
        x = 0.0f;
        y = 0.0f;
        rotation = 0.0f;
    }
}