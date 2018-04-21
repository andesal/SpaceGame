package no.progark19.spacegame.interfaces;

import no.progark19.spacegame.utils.RenderableWorldState;

/**
 * Created by Anders on 16.04.2018.
 */

public interface ReceivedDataListener {
    void onReceive(RenderableWorldState data);

    void onReceive(String data);
}
