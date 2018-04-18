package no.progark19.spacegame.interfaces;

import com.badlogic.gdx.utils.Json;

/**
 * Created by Anders on 16.04.2018.
 */

public interface P2pConnector {
    void setThisDeviceName(String name);

    void discoverPeers();

    boolean hasConnection();

    void addReceivedDataListener(ReceivedDataListener listener);

    void sendData(Json data);

    void sendData(String data);

    String getOtherPeerName();
}
