package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

import java.awt.Rectangle;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class SpriteComponent implements Component, Pool.Poolable {
    public Sprite sprite;

    public SpriteComponent() {}

    public SpriteComponent(Texture texture, float scale) {
        sprite = new Sprite(texture);
        sprite.setScale(scale);
    }

    public SpriteComponent(Sprite sprite){
        this.sprite = sprite;
    }


    @Override
    public void reset() {

    }

}
