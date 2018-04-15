package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Anders on 14.04.2018.
 */

public class ParentComponent implements Component {
    public int parentID;

    public ParentComponent(int parentID) {
        this.parentID = parentID;
    }
}
