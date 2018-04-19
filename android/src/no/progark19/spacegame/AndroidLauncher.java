package no.progark19.spacegame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    private AndroidP2pConnector connector;



    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (connector == null) {
            connector = new AndroidP2pConnector(this);
        }

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new SpaceGame(connector), config);
	}

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
