package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Anders on 14.04.2018.
 */

public class BodyComponent implements Component, Pool.Poolable {

    public Body body;

    public BodyComponent() {}

    public BodyComponent(Body body) {
        this.body = body;
    }


    @Override
    public void reset() {

    }
}
