package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class GravityComponent implements Component {

    public int gravity;

    public GravityComponent(int gravity) {
        this.gravity = gravity;
    }
}
