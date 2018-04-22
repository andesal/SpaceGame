package no.progark19.spacegame.utils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 20.04.2018.
 */

public class RenderableWorldState implements Serializable {
    private List<float[]> states;

    public RenderableWorldState(int size) {
        states = new ArrayList<float[]>(size);
    }

    public void addState(float[] state){
        states.add(state);
    }

    public List<float[]> getStates() {
        return states;
    }
}
