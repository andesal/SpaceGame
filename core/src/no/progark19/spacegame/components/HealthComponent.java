package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class HealthComponent implements Component, Pool.Poolable {

    public int health = 100;

    public HealthComponent() {}

    public HealthComponent(int health) {
        this.health = health;
    }

    @Override
    public void reset() {

    }
}
