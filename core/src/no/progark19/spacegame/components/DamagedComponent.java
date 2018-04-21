package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by anderssalvesen on 20.04.2018.
 */

public class DamagedComponent implements Component {

    public int damage;

    public DamagedComponent(int damage) {
        this.damage = damage;
    }


}
