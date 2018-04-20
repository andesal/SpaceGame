package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by anderssalvesen on 20.04.2018.
 */

public class FuelUsageComponent implements Component {

    public float usage;

    public FuelUsageComponent(float usage) {
        this.usage = usage;
    }

}
