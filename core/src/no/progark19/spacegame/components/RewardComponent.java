package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by anderssalvesen on 20.04.2018.
 */

public class RewardComponent implements Component {

    public int reward;
    public String type;


    public RewardComponent(int reward, String type) {
        this.reward = reward;
        this.type = type;
    }
}
