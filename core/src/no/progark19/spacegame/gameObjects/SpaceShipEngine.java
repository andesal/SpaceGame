package no.progark19.spacegame.gameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Anders on 10.04.2018.
 */

public class SpaceShipEngine {
    public final static Vector2 ENGINE_ORIGIN = new Vector2(9,25);
    public final static float ENGINE_MAX_FORCE = 0.1f;
    private Vector2 relativePosition;
    private Vector2 position;
    private float rotation;
    private Sprite sprite_engine;
    private float[] rotationLimits;
    private float relativeAngle;
    private float engineRotation;

    boolean engineOn = false;

    public float getRotation() {
        return rotation;
    }

    public SpaceShipEngine(Vector2 relativePosition, Sprite sprite_engine, float rotationMin, float rotationMax) {
        this.relativePosition = relativePosition;
        this.relativeAngle = this.relativePosition.angle();
        this.position = new Vector2(0,0);
        this.sprite_engine = sprite_engine;
        rotationLimits = new float[]{rotationMin, rotationMax, rotationMax-rotationMin};
        setRotationByPercentage(50);
    }

    public Vector2 getOriginWorldpoint(){
        return new Vector2(
                position.x,
                position.y
        );
    }

    public void setSpriteOrigin(float x, float y){
        sprite_engine.setOrigin(x, y);
    }

    public void setRotationByPercentage(float degreePercentage){
        engineRotation = rotationLimits[0] + rotationLimits[2]*degreePercentage/100f;
    }

    protected void placeRelativeTo(float x, float y){
        position.set(x + relativePosition.x, y + relativePosition.y);
    }

    protected void rotateRelativeTo(float degrees){
        relativePosition.setAngle(degrees + relativeAngle);
        rotation = ((engineRotation % 360) + (degrees % 360));
    }

    public void draw(SpriteBatch batch){
        sprite_engine.setOriginBasedPosition(position.x, position.y);
        sprite_engine.setRotation(rotation);

        sprite_engine.draw(batch);
    }

    public Vector2 getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Vector2 relativePosition) {
        this.relativePosition = relativePosition;
    }
}
