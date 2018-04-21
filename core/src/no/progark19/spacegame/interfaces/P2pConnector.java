package no.progark19.spacegame.interfaces;

import no.progark19.spacegame.utils.RenderableWorldState;

/**
 * Created by Anders on 16.04.2018.
 */

public interface P2pConnector {
    void setThisDeviceName(String name);

    void discoverPeers();

    boolean hasConnection();

    void addReceivedDataListener(ReceivedDataListener listener);

    void removeReceivedDataListener(ReceivedDataListener listener);

    void sendData(RenderableWorldState data);

    void sendData(String data);

    String getOtherPeerName();

    int decideLeadingPeer();
}
