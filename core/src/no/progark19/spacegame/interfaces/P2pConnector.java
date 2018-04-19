package no.progark19.spacegame.interfaces;

import com.badlogic.gdx.utils.Json;

import no.progark19.spacegame.utils.json.JsonPayload;

/**
 * Created by Anders on 16.04.2018.
 */

public interface P2pConnector {
    void setThisDeviceName(String name);

    void discoverPeers();

    boolean hasConnection();

    void addReceivedDataListener(ReceivedDataListener listener);

    void removeReceivedDataListener(ReceivedDataListener listener);

    void sendData(JsonPayload data);

    void sendData(String data);

    String getOtherPeerName();
}
