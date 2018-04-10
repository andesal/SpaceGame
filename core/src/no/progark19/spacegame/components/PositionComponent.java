package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class PositionComponent implements Component {
    public float x = 0.0f;
    public float y = 0.0f;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
