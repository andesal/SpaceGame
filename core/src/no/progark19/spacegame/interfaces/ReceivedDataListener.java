package no.progark19.spacegame.interfaces;

import com.badlogic.gdx.utils.Json;

import no.progark19.spacegame.utils.json.JsonPayload;

/**
 * Created by Anders on 16.04.2018.
 */

public interface ReceivedDataListener {
    void onReceive(JsonPayload data);

    void onReceive(String data);
}
