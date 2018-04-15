package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class PowerupComponent implements Component, Pool.Poolable{

    private String type;

    public PowerupComponent() {}

    public void setType(String type) {

    }

    @Override
    public void reset() {

    }


}
