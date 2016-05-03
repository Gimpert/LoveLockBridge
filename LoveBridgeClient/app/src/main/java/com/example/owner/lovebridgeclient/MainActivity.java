package com.example.owner.lovebridgeclient;

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.concurrent.ExecutionException;

import android.os.Handler;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ServerRelay serverRelay;
    GoogleApiClient locationClient = null;
    LocationRequest mLocationRequest = null;
    private static final int REQUEST_LOCATION = 0;
    private static int UPDATE_INTERVAL = 10000, FATEST_INTERVAL = 5000, DISPLACEMENT = 0;
    Button submitSetupButton;
    float range = 100;
    AnimationSet lockSlide;
    AnimationSet lockRotate;
    ImageView lock1;
    Animation.AnimationListener animationListener;



    ArrayList<String> oldNearbyClients;
    ArrayList<String> newNearbyClients;
    String bridgeName = "default";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        new Handler().postDelayed(new Runnable() {
            public void run() {

                //setupMenu();
            }
        }, 100);


        //Initialize the Location Service

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        GridLayout gridLayout = (GridLayout) findViewById(R.id.main_layout);
        lock1 = (ImageView) gridLayout.findViewById(R.id.lock1);
        lockSlide =  (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.lock_slide);
        lockSlide.setInterpolator(new LinearInterpolator());

        lockRotate = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.lock_rotate);
        lockSlide.setInterpolator(new LinearInterpolator());

        lockSlide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                lock1.setTranslationX(-45);
                lock1.setImageResource(R.drawable.lock_md_96);


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lock1.setTranslationX(0);
                if (BridgeProximity.getInstance().isBridgeProximity()){
                    lock1.startAnimation(lockRotate);
                }else {
                    lock1.startAnimation(lockSlide);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lockRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                lock1.setRotation(-45);
                lock1.setImageResource(R.drawable.lock_n_key_96);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lock1.setRotation(0);
                if (BridgeProximity.getInstance().isBridgeProximity()){
                    lock1.startAnimation(lockRotate);
                }else {
                    lock1.startAnimation(lockSlide);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        new HttpAsyncTask().execute();
        lock1.startAnimation(lockSlide);


    }

    public void setupMenu() {
        submitSetupButton = (Button) findViewById(R.id.submit_setup);

        final LayoutInflater inflater = LayoutInflater.from(this);

        //final View contentMainView = inflater.inflate(R.layout.activity_main, null, false);

        final View popupView = inflater.inflate(R.layout.setup_menu, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupView, 1, 1, true);
        final EditText etRangeString = (EditText) popupView.findViewById(R.id.range);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        submitSetupButton = (Button) popupView.findViewById(R.id.submit_setup);
        submitSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    range = Float.parseFloat(etRangeString.getText().toString());
                    popupWindow.dismiss();
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Range needs to be a decimal number", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                locationClient);
        if (lastLocation != null) {
            BridgeProximity.getInstance().setCurrentlocation(lastLocation);

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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.owner.lovebridgeclient/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.owner.lovebridgeclient/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        public HttpAsyncTask() {

        }

        protected String doInBackground(String... params) {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                return sendLocation();
            }
        }

        protected String sendLocation() {
            Location currentLocation = BridgeProximity.getInstance().getCurrentlocation();
            String result = "";
            if (currentLocation != null) {
                result = ServerRelay.pingServer(bridgeName, "" + currentLocation.getLatitude(), "" + currentLocation.getLongitude(), "" + range);
            }
            newNearbyClients = ResponseParser.parseResponse(result);
            if(!newNearbyClients.isEmpty()){
                BridgeProximity.getInstance().setBridgeProximity(true);
            }else{
                BridgeProximity.getInstance().setBridgeProximity(false);
            }
            return null;
        }
    }

    public boolean isNewClientNearby(String result) {
        oldNearbyClients = newNearbyClients;
        newNearbyClients = ResponseParser.parseResponse(result);

        for (String client : newNearbyClients) {
            if (!oldNearbyClients.contains(client)) return true;
        }

        return false;
    }
}
