package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;


public class ElementComponent implements Component, Pool.Poolable {

    public Enum element;

    public ElementComponent(Enum element) {
        this.element = element;
    }


    @Override
    public void reset() {

    }

}
