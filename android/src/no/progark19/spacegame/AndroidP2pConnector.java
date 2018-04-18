package no.progark19.spacegame;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.utils.Json;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate.Status;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.LinkedList;
import java.util.List;

import no.progark19.spacegame.interfaces.P2pConnector;
import no.progark19.spacegame.interfaces.ReceivedDataListener;

/**
 * Created by Anders on 17.04.2018.
 */

public class AndroidP2pConnector implements P2pConnector {
    private final  String TAG = "AndroidConnector";

    private List<ReceivedDataListener> dataListeners = new LinkedList<>();
    
    private AndroidLauncher launcher;

    private static final Strategy STRATEGY = Strategy.P2P_POINT_TO_POINT;

    private ConnectionsClient connectionsClient;
    private String deviceName = "Henry";

    private String otherPlayerEndpointId;
    private String otherPLayerName = "null";

    private Json otherPlayerInput;
    private Json userPlayerInput;

    private boolean isConnected = false;
    
    // Callback to receive payloads
    private final PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            Toast.makeText(launcher, "Recieved payload from " + s, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onPayloadReceived: Recieved payload!");

            for (ReceivedDataListener dListener: dataListeners){
                //noinspection ConstantConditions
                dListener.onReceive(new String(payload.asBytes()));
            }
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate pltUpdate) {
            Log.d(TAG, "onPayloadTransferUpdate: " + pltUpdate.getStatus() + "\n ->" +
                    pltUpdate.getBytesTransferred() + "/" + pltUpdate.getTotalBytes());
            if (pltUpdate.getStatus() == Status.SUCCESS){
                Log.d(TAG, "onPayloadTransferUpdate: Finished download");
            }
        }
    };

    // Callback for finding other devices
    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String s, @NonNull DiscoveredEndpointInfo deInfo) {
            Log.d(TAG, "onEndpointFound: endpoint found, connecting!");
            connectionsClient.requestConnection(deviceName, s, connectionLifecycleCallback);
        }

        @Override
        public void onEndpointLost(@NonNull String s) {
            //TODO anything?
        }
    };

    // Callback for connecting to other devices
    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
            Log.d(TAG, "onConnectionInitiated: accepting connection");
            connectionsClient.acceptConnection(s, payloadCallback);
            otherPLayerName = connectionInfo.getEndpointName();
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            if (connectionResolution.getStatus().isSuccess()){
                Toast.makeText(launcher, "Connection successful!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onConnectionResult: connection successful!");

                connectionsClient.stopDiscovery();
                connectionsClient.stopAdvertising();

                otherPlayerEndpointId = s;
                isConnected = true;


            } else {
                Toast.makeText(launcher, "Connection failed!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onConnectionResult: connection failed!");

            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            Log.d(TAG, "onDisconnected: Disconnected from other player");
            Toast.makeText(launcher, "You got separated", Toast.LENGTH_SHORT).show();
        }
    };

    public AndroidP2pConnector(AndroidLauncher androidLauncher) {

        this.launcher = androidLauncher;
        connectionsClient = Nearby.getConnectionsClient(launcher);

    }

    private void startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        DiscoveryOptions.Builder builder = new DiscoveryOptions.Builder();
        connectionsClient.startDiscovery(
                launcher.getPackageName(), endpointDiscoveryCallback, builder.setStrategy(STRATEGY).build());
    }

    /** Broadcasts our presence using Nearby Connections so other players can find us. */
    private void startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        AdvertisingOptions.Builder builder = new AdvertisingOptions.Builder();
        connectionsClient.startAdvertising(
                deviceName, launcher.getPackageName(), connectionLifecycleCallback, builder.setStrategy(STRATEGY).build());
    }

    @Override
    public void setThisDeviceName(String name) {
        deviceName = name;
    }

    @Override
    public void discoverPeers() {
        startAdvertising();
        startDiscovery();
    }

    @Override
    public void addReceivedDataListener(ReceivedDataListener listener) {
        dataListeners.add(listener);
    }

    @Override
    public void sendData(Json data) {
        String jsonString = data.toString();

        connectionsClient.sendPayload(otherPlayerEndpointId, Payload.fromBytes(jsonString.getBytes()));
    }

    @Override
    public void sendData(String data) {
        connectionsClient.sendPayload(otherPlayerEndpointId, Payload.fromBytes(data.getBytes()));
    }

    @Override
    public String getOtherPeerName() {
        return otherPLayerName;
    }

    @Override
    public boolean hasConnection() {
        return isConnected;
    }
}
