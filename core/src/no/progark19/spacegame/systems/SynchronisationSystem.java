package no.progark19.spacegame.systems;

import com.badlogic.ashley.systems.IntervalSystem;

/**
 * Created by Anders on 19.04.2018.
 */

public class SynchronisationSystem extends IntervalSystem {

    public SynchronisationSystem(float interval) {
        super(interval);
    }

    public SynchronisationSystem(float interval, int priority) {
        super(interval, priority);
    }

    @Override
    protected void updateInterval() {

    }
}
