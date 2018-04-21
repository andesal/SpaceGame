package no.progark19.spacegame.utils.json;

/**
 * Created by Anders on 20.04.2018.
 */

public interface WorldStateIndexes {
    int WS_NUMBER_OF_VALUES = 7;

    int WS_ENTITYID = 0;
    //Use these when saving a render-able worldState
    int WS_RENDERABLE_POSX = 1;
    int WS_RENDERABLE_POSY = 2;
    int WS_RENDERABLE_ROTATION = 3;

    int WS_RENDERABLE_VX = 4;
    int WS_RENDERABLE_VY = 5;
    int WS_RENDERABLE_VR = 6;
}
