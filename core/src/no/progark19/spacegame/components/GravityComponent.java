package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class GravityComponent implements Component, Pool.Poolable {

    public int gravity;

    public GravityComponent(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public void reset() {

    }
}
