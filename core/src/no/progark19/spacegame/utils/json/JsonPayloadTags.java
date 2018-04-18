package no.progark19.spacegame.utils.json;

/**
 * Created by Anders on 18.04.2018.
 */

public interface JsonPayloadTags {
    //Tag to signify that a user is ready to play
    int READY = 1;
    //Suggesting a seed for the random generator
    int GAME_SEED = 2;
    //Accepting a seed
    int ACCEPT_SEED = 3;
    //Tag for giving new Engine Info
    int SLIDER_UPDATE = 4;
    //Tag for
}
