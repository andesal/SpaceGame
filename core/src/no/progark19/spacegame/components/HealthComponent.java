package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by anderssalvesen on 09.04.2018.
 */

public class HealthComponent implements Component {

    public int health;

    public HealthComponent(int health) {
        this.health = health;
    }
}
