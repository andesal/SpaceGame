package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import java.awt.Rectangle;

/**
 * Created by Anders on 15.04.2018.
 */

public class BoundsComponent implements Component, Pool.Poolable{

    public Rectangle rectangle;

    @Override
    public void reset() {

    }
}
