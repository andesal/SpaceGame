package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class PositionComponent implements Component, Poolable {
    public float x = 0.0f;
    public float y = 0.0f;
    public float rotation = 0;

    public PositionComponent() {}

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
    }

    public PositionComponent(float x, float y, float rotation) {
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
