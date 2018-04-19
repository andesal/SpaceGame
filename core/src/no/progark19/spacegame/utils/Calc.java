package no.progark19.spacegame.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import no.progark19.spacegame.SpaceGame;

/**
 * Created by anderssalvesen on 17.04.2018.
 */

public class Calc {

    public static Vector3 translateScreenCoordinates(Vector3 coordinates, OrthographicCamera camera){
        return camera.unproject(coordinates.add(0, SpaceGame.HEIGHT - 1, 0));
    }
}
