package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class VelocityComponent implements Component {

    public float velocity = 0.0f;

    public VelocityComponent(float velocity) {
        this.velocity = velocity;
    }



}
