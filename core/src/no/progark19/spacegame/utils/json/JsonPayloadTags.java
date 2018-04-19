package no.progark19.spacegame.utils.json;

/**
 * Created by Anders on 18.04.2018.
 */

public interface JsonPayloadTags {
    //Sends this players readyStatus
    int READY = 1;
    //Sending this devices seed
    int GAME_SEED = 2;
    //Tag for giving new Engine Info
    int ENGINE_ROTATION_UPDATE = 3;
    //Tag for turning on an engine
    int ENGINE_ON_UPDATE = 4;

    String ENGINE_UPDATE_ENGINEID = "EU1";
    String ENGINE_ROTATION_UPDATE_ROTATION = "EU2";
    String ENGINE_ROTATION_UPDATE_FORCEDIRECTION = "EU3";

    String ENGINE_ON_UPDATE_ISON = "EU4";
}
