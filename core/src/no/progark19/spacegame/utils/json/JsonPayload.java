package no.progark19.spacegame.utils.json;

/**
 * Created by Anders on 18.04.2018.
 */

public class JsonPayload {
    private int TAG;
    private Object value;

    public int getTAG() {
        return TAG;
    }

    public void setTAG(int TAG) {
        this.TAG = TAG;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

