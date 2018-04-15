package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

/**
 * Created by anderssalvesen on 15.04.2018.
 */

public class AnimationComponent implements Component, Pool.Poolable{

    public final Array<TextureRegion> frames = new Array<TextureRegion>();

    @Override
    public void reset() {

    }
}
