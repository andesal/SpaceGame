package no.progark19.spacegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import no.progark19.spacegame.SpaceGame;
import no.progark19.spacegame.interfaces.P2pConnector;
import no.progark19.spacegame.interfaces.ReceivedDataListener;
import no.progark19.spacegame.utils.RenderableWorldState;
import no.progark19.spacegame.utils.json.JsonPayload;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 20;
		config.width = SpaceGame.WIDTH;
		config.height = SpaceGame.HEIGHT;
		config.resizable = false;
		new LwjglApplication(new SpaceGame(new P2pConnector() {
			@Override
			public void setThisDeviceName(String name) {

			}

			@Override
			public void discoverPeers() {

			}

			@Override
			public boolean hasConnection() {
				return false;
			}

			@Override
			public void addReceivedDataListener(ReceivedDataListener listener) {

			}

			@Override
			public void removeReceivedDataListener(ReceivedDataListener listener) {

			}

			@Override
			public void sendData(RenderableWorldState data) {

			}

			@Override
			public void sendData(String data) {

			}

			@Override
			public String getOtherPeerName() {
				return null;
			}

			@Override
			public void disconnect() {

			}

			public int decideLeadingPeer() {
				return 0;
			}
		}), config);
	}
}
