package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Anders on 14.04.2018.
 */

public class TextureComponent implements Component, Pool.Poolable{

    public Texture texture;

    @Override
    public void reset() {

    }
}
