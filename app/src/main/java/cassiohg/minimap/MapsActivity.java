package cassiohg.minimap;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback,
        ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleMap mMap;
    private boolean isMapReady = false;

    private GoogleApiClient mGoogleApiClient;
    private boolean isLocationReady = false;
    private Location mLastLocation;
    protected static final String TAG = "Google Location API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        buildGoogleMapInstance();
        buildGoogleApiClient();

        toObject(getIntent().getStringExtra("jsonObject"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        isMapReady = true;
        mMap = map;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng[] members = new LatLng[]{new LatLng(-22.919121, -43.411188),
                                        new LatLng(-22.917599, -43.403742),
                                        new LatLng(-22.921256, -43.415930),
                                        new LatLng(-22.921730, -43.413742),
                                        new LatLng(-22.921730, -43.413742)};
        Marker[] markers = new Marker[members.length];
        for (int i = 0; i < members.length; i++) {
            markers[i] = mMap.addMarker(new MarkerOptions().position(members[i]).title("label "+i));
        }
        setCameraInitialPosition();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        isLocationReady = true;
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            setCameraInitialPosition();
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    private void setCameraInitialPosition() {
        if (isMapReady && isLocationReady) {
            Double lat = mLastLocation.getLatitude();
            Double lng = mLastLocation.getLongitude();
            latestLatitude = lat; // setting user information.
            latestLongitude = lng; // setting user information.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected synchronized void buildGoogleMapInstance() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}