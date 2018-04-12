package no.progark19.spacegame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

import no.progark19.spacegame.screens.PlayScreen_DEMO;

/**
 * Created by Anders on 10.04.2018.
 */

public class SpaceShip {
    private Sprite sprite_baseShip;
    private Body body_baseShip;
    private Array<SpaceShipEngine> engines;
    private final int maxSpeed = 1;
    private Affine2 affine;

    Sprite controllDebugOverlay = new Sprite(new Texture("img/playscreenTestDebugOverlay.png"));

    public void setBody_baseShip(Body body_baseShip) {
        this.body_baseShip = body_baseShip;
    }

    public SpaceShip(Sprite baseShipSprite) {
        sprite_baseShip = baseShipSprite;
        engines = new Array<SpaceShipEngine>();
        affine = new Affine2();
    }

    public void addEngine(Vector2 relativePosition, Sprite engineSprite, float rotationMin, float rotationMax){
        engines.add(new SpaceShipEngine(relativePosition, engineSprite, rotationMin, rotationMax));
    }

    public void draw(SpriteBatch batch){
        sprite_baseShip.draw(batch);
        for (SpaceShipEngine engine: engines) {
            engine.draw(batch);
            System.out.println(engine.getOriginWorldpoint());
            if (engine.engineOn) {
                controllDebugOverlay.draw(batch);
            }
        }
        System.out.println("-------------------------------------------");
    }

    //Getters and setters --------------------------------------------------------------------------
    public void setRotation(float degrees){
        sprite_baseShip.setRotation(degrees);
        //affine.setToRotation(degrees);
    }

    public Vector2 getOriginWorldpoint(){
        return new Vector2(
                sprite_baseShip.getX() + sprite_baseShip.getOriginX(),
                sprite_baseShip.getY() + sprite_baseShip.getOriginY()
        );
    }

    public float getRotation(){
        return sprite_baseShip.getRotation();
    }

    public float getX() {
        return sprite_baseShip.getX();
    }

    public float getY() {
        return sprite_baseShip.getY();
    }

    public void setPosition(float x, float y) {
        sprite_baseShip.setPosition(x, y);
        for (SpaceShipEngine engine: engines) {
            engine.rotateRelativeTo(sprite_baseShip.getRotation());
            engine.placeRelativeTo(x + sprite_baseShip.getOriginX(),
                                   y + sprite_baseShip.getOriginY());

            //Fixme remove
            if(engine.engineOn) {
                Vector2 force = new Vector2(SpaceShipEngine.ENGINE_MAX_FORCE,0.0f);
                force.setAngle(engine.getRotation());
                force.rotate(90);

                System.out.println(force);
                System.out.println(engine.getRotation());
                //body_baseShip.applyForceToCenter(force,true);
                //FIXME REMOVE DEBUG
                controllDebugOverlay.setOriginBasedPosition(engine.getOriginWorldpoint().x, engine.getOriginWorldpoint().y);
                controllDebugOverlay.setRotation(force.angle());


                body_baseShip.applyForce(
                        force, engine.getOriginWorldpoint().scl(1/PlayScreen_DEMO.PIXELS_TO_METERS), true
                );

                if(body_baseShip.getLinearVelocity().len() > maxSpeed){
                    body_baseShip.getLinearVelocity().setLength(maxSpeed);
                }
            }
        }
    }

    public void changeEngineAngle (int engineIndex, float anglePercentage){
        //System.out.println("Setting engine [" + engineIndex + "] to percentage " + anglePercentage);
        engines.get(engineIndex).setRotationByPercentage(anglePercentage);
    }

    public Sprite getsprite_baseShip() {
        return sprite_baseShip;
    }


    public void setEngineOn(int i, boolean b) {
        engines.get(i).engineOn = b;
    }
    //----------------------------------------------------------------------------------------------
}
