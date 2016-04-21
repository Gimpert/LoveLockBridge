package com.example.owner.lovebridgeclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ServerRelay serverRelay;
    GoogleApiClient locationClient = null;
    LocationRequest mLocationRequest = null;
    private static final int REQUEST_LOCATION = 0;
    private static int UPDATE_INTERVAL = 10000,FATEST_INTERVAL = 5000, DISPLACEMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverRelay = new ServerRelay();

        if (locationClient == null) {
            locationClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            createLocationRequest();

        }

        //Initialize the Location Service
        new HttpAsyncTask().execute();
    }
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , REQUEST_LOCATION);
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                locationClient);
        if (lastLocation != null) {
            BridgeProximity.getInstance().setCurrentlocation(lastLocation);
            new HttpAsyncTask().execute();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                locationClient, mLocationRequest, this);
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        BridgeProximity prox = BridgeProximity.getInstance();
        prox.setCurrentlocation(location);
        new HttpAsyncTask().execute();
    }



    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        public HttpAsyncTask() {

        }
        protected String doInBackground(String... params) {
            while (true) {
                sendLocation();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
            }
        }

        protected String sendLocation() {
            Location currentLocation = BridgeProximity.getInstance().getCurrentlocation();
            String result = "";
            if (currentLocation != null) {
                result = ServerRelay.pingServer("" + currentLocation.getLatitude(), "" + currentLocation.getLongitude());
            }
            //TODO: initialize animation
            //if (there is a new string in the result compared to previous result) initialize animation
        }
    }
}
