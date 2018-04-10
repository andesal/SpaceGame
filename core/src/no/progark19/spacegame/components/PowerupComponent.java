package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class PowerupComponent implements Component {

    public static enum TYPE {
        HEALTH, FUEL, WEAPON;
    }

    public Enum type;
    public int duration = 0;

    public PowerupComponent(Enum type, int duration) {
        this.type = type;
        this.duration = duration;
    }

}
