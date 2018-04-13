package no.progark19.spacegame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Anders on 12.04.2018.
 */

public class GameSettings {
    public static final boolean CAMERA_FOLLOW_POSITION = true;
    public static final boolean CAMERA_FOLLOW_ROTATION = true;
    public static final boolean BOX2D_DRAWDEBUG = true;

    public static final boolean SPACESHIP_STABILIZE_ROTATION = true;
    public static final float SPACESHIP_STABILIZATION_SCALAR = 0.995f;
    public static final boolean SPACESHIP_ENABLE_ROTATION = false;


    public final static Vector2 ENGINE_ORIGIN = new Vector2(9,25);
    public final static float ENGINE_MAX_FORCE = 0.1f;

}
