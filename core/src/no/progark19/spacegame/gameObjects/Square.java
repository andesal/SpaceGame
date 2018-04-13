package no.progark19.spacegame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by anderssalvesen on 13.04.2018.
 */

public class Square {

    public ArrayList<Sprite> stuffs;

    public Texture texture;

    public float posX;
    public float posY;

    public int id;

    public Square(boolean mod2, int posX, int posY) {
        if (mod2) {
            texture = new Texture("img/bg1.jpg");
        } else {
            texture = new Texture("img/bg2.jpg");
        }
        this.posX = posX;
        this.posY = posY;

    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, posX, posY);
    }


}
