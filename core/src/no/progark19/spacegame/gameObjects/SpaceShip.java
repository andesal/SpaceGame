package no.progark19.spacegame.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import no.progark19.spacegame.GameSettings;
import no.progark19.spacegame.screens.PlayScreen_DEMO;

/**
 * Created by Anders on 10.04.2018.
 */

public
class SpaceShip {
    private Sprite sprite_baseShip;
    private Body body_baseShip;
    private Array<SpaceShipEngine> engines;
    private final int maxSpeed = 1;

    //Sprite controllDebugOverlay = new Sprite(new Texture("img/playscreenTestDebugOverlay.png"));

    public SpaceShip(Sprite baseShipSprite) {
        sprite_baseShip = baseShipSprite;
        engines = new Array<SpaceShipEngine>();
    }

    public void draw(SpriteBatch batch){
        int i = 0; //Checks how many engines are on
        sprite_baseShip.draw(batch);
        for (SpaceShipEngine engine: engines) {
            engine.draw(batch);
            if (engine.engineOn) {
                i++;

                Vector2 force = new Vector2(GameSettings.ENGINE_MAX_FORCE,0.0f);
                force.setAngle(engine.getRotation());
                force.rotate(90);

                //body_baseShip.applyForceToCenter(force,true);
                //FIXME REMOVE DEBUG
                //controllDebugOverlay.setOriginBasedPosition(engine.getOriginWorldpoint().x, engine.getOriginWorldpoint().y);
                //controllDebugOverlay.setRotation(force.angle());
                //controllDebugOverlay.draw(batch);


                if (GameSettings.SPACESHIP_ENABLE_ROTATION) {
                    body_baseShip.applyForce(
                        force, engine.getOriginWorldpoint().scl(1/GameSettings.BOX2D_PIXELS_TO_METERS), true
                    );
                } else {
                    body_baseShip.applyForceToCenter(
                            force, true
                    );
                }

                if(body_baseShip.getLinearVelocity().len() > maxSpeed){
                    body_baseShip.getLinearVelocity().setLength(maxSpeed);
                }
            }
        }

        //FIXME Dette burde ikke være her
        if (GameSettings.SPACESHIP_STABILIZE_ROTATION && i == 0 && Math.abs(body_baseShip.getAngularVelocity()) > 0) {
            System.out.println(body_baseShip.getAngularVelocity());
            float newAV = body_baseShip.getAngularVelocity()* GameSettings.SPACESHIP_STABILIZATION_SCALAR;
            if (Math.abs(newAV) <= 1E-3) {newAV = 0 ;}
            body_baseShip.setAngularVelocity(newAV);
        }
    }

    public void changeEngineAngle (int engineIndex, float anglePercentage){
        engines.get(engineIndex).setRotationByPercentage(anglePercentage);
    }

    //Getters and setters --------------------------------------------------------------------------
    public void setBody_baseShip(Body body_baseShip) {
        this.body_baseShip = body_baseShip;
    }

    public void addEngine(Vector2 relativePosition, Sprite engineSprite, float rotationMin, float rotationMax){
        engines.add(new SpaceShipEngine(relativePosition, engineSprite, rotationMin, rotationMax));
    }

    public void setRotation(float degrees){
        sprite_baseShip.setRotation(degrees);
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


        }
    }


    public Sprite getsprite_baseShip() {
        return sprite_baseShip;
    }

    public float getWidth(){
        return sprite_baseShip.getWidth();
    }

    public float getHeight(){
        return sprite_baseShip.getHeight();
    }

    public void setEngineOn(int i, boolean b) {
        engines.get(i).engineOn = b;
    }
    //----------------------------------------------------------------------------------------------
}
