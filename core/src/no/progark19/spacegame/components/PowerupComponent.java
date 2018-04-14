package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class PowerupComponent implements Component, Pool.Poolable{

    public static enum TYPE {
        HEALTH, FUEL, WEAPON;
    }


    public PowerupComponent() {

    }


    @Override
    public void reset() {

    }


}
