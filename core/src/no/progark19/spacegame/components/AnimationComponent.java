package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by anderssalvesen on 15.04.2018.
 */

public class AnimationComponent implements Component, Pool.Poolable{

    public Array<TextureRegion> frames;
    public int frame;
    public Animation animation;

    public AnimationComponent(Animation animation) {
        this.animation = animation;
    }

    public TextureRegion getCurrentFrame() {
        return frames.get(frame);
    }

    public TextureRegion getFrame(int frame) {
        return frames.get(frame);
    }

    @Override
    public void reset() {
        frame = 0;
    }
}
