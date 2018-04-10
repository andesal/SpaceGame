package no.progark19.spacegame.components;

import com.badlogic.ashley.core.Component;


public class ElementComponent implements Component {

    public Enum element;

    public static enum ELEMENTS {
        FIRE, ICE, SPECIAL
    }

    public ElementComponent(Enum element) {
        this.element = element;
    }

}
