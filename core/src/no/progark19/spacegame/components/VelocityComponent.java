package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class VelocityComponent implements Component, Pool.Poolable {

    public float velx = 0;
    public float vely = 0;
    public float velAngle = 0;

    @Override
    public void reset() {
        velx = 0;
        vely = 0;
    }
}
