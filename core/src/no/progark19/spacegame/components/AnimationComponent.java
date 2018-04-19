package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import no.progark19.spacegame.systems.AnimationSystem;

/**
 * Created by anderssalvesen on 15.04.2018.
 */

public class AnimationComponent implements Component, Pool.Poolable{

    public TextureRegion region;
    public Array<TextureRegion> frames;

    public int frameCount;
    public float maxFrameTime;
    public float currentFrameTime;
    public int frame;
    public int frameWidth;
    public int frameHeight;
    private boolean dirRight;

    public Animation animation;

    public AnimationComponent(Animation animation) {
        this.animation = animation;
    }

    /*
    public AnimationComponent (TextureRegion region, int rows, int cols, float cycleTime, boolean direction) {
        this.region = region;
        this.dirRight = direction;
        frames = new Array<TextureRegion>();

        frameWidth = region.getRegionWidth()/cols;
        frameHeight = region.getRegionHeight()/rows;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)  {
                TextureRegion r = new TextureRegion(region, j * frameWidth, i * frameHeight, frameWidth, frameHeight);
                if (dirRight) {
                    r.flip(true, false);
                }
                frames.add(r);
            }
        }
        frameCount = rows * cols;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }
    */

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
