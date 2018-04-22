package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import no.progark19.spacegame.utils.MyProgressBar;

/**
 * Created by Anders on 22.04.2018.
 */

public class HealthbarComponent implements Component, Poolable {
    public MyProgressBar bar = null;

    @Override
    public void reset() {
        bar = null;
    }
}
