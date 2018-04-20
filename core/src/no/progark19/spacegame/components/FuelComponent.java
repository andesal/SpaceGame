package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by anderssalvesen on 20.04.2018.
 */

public class FuelComponent implements Component, Pool.Poolable {

    public float fuel;

    public FuelComponent(float fuel) {
        this.fuel = fuel;
    }

    @Override
    public void reset() {

    }
}
