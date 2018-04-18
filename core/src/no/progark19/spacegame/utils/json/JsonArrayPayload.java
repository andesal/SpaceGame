package no.progark19.spacegame.utils.json;

import com.badlogic.gdx.utils.Array;

public class JsonArrayPayload {
    private String TAG;
    private Array<Object> objects;

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public Array<Object> getObjects() {
        return objects;
    }

    public void setObjects(Array<Object> objects) {
        this.objects = objects;
    }
}
