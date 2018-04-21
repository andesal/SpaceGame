package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Anders on 19.04.2018.
 */

public class SynchronizedComponent implements Component, Pool.Poolable {
    //Empty marker component

    @Override
    public void reset() {  }
}
