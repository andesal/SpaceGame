package no.progark19.spacegame.utils.json;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Anders on 18.04.2018.
 */

public class JsonPayload implements Serializable {
    private int TAG;
    private Object value;
    //private HashMap<String, Object> values;

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

