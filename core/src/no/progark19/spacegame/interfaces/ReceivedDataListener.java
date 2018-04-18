package no.progark19.spacegame.interfaces;

import com.badlogic.gdx.utils.Json;

/**
 * Created by Anders on 16.04.2018.
 */

public interface ReceivedDataListener {
    void onReceive(Json data);

    void onReceive(String data);
}
