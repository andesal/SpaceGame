package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class RotationComponent implements Component {

    public int rotation;

    public RotationComponent(int rotation) {
        this.rotation = rotation;
    }

}
