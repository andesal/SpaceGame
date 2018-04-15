package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Anders on 15.04.2018.
 */

public class ForceApplierComponent implements Component, Pool.Poolable {
    public float force;
    public float direction;
    public int targetEntity;

    public ForceApplierComponent(float force) {
        this.force = force;
    }

    @Override
    public void reset() {

    }
}
