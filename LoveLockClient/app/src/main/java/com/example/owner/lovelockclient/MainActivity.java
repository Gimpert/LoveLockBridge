package com.example.owner.lovelockclient;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.example.owner.BridgeCommunication.ServerRelay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static Context CONTEXT;

    KeyListAdapter listAdapter;
    ListView keyListView;

    GoogleApiClient locationClient = null;

    private LocationRequest mLocationRequest;

    private static int UPDATE_INTERVAL = 10000,FATEST_INTERVAL = 5000, DISPLACEMENT = 0;

    private static final int REQUEST_LOCATION = 0;
    TextView etResponse;
    LockList lockList;
    //String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        CONTEXT = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (locationClient == null) {
            locationClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            createLocationRequest();

        }

        //TODO: debug functionality, remove later
        etResponse = (TextView) findViewById(R.id.etResponse);


        //Initialize the Location Service


        new HttpAsyncTask().execute();

        //Initialize the lock list, and load saved locks
        lockList = LockList.getInstance();
        lockList.loadLocks();
        prepareListData();

        //Initialize the listview displaying the user's locks
        keyListView = (ListView) findViewById(R.id.key_list_view);
        listAdapter = new KeyListAdapter(this, lockList.getList());

        keyListView.setAdapter(listAdapter);

        keyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String testToast = ((Lock) keyListView.getItemAtPosition(position)).getName();
                Toast.makeText(MainActivity.this, testToast, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION} ,REQUEST_LOCATION);
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                locationClient);
        if (lastLocation != null) {
            BridgeProximity.getInstance().setCurrentlocation(lastLocation);
            new HttpAsyncTask().execute();
        }

    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    protected void onStart() {
        locationClient.connect();
        super.onStart();
    }
    @Override
    protected void onStop() {
        locationClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    //If the current location is not null, executes a check with the server to see if a bridge
    //is in range
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Location currentLocation = BridgeProximity.getInstance().getCurrentlocation();
            boolean inRange = false;
            if(currentLocation != null){
                inRange = ServerRelay.isInBridgeRange("" + currentLocation.getLatitude(), "" + currentLocation.getLongitude());
            }
            BridgeProximity.getInstance().setBridgeProximity(inRange);
            return null;
        }
    }

    public static Context getContext() {
        return CONTEXT;
    }


    private void prepareListData() {

//        Iterator<Lock> it = lockList.getList().iterator();
//        while (it.hasNext()) {
//            Lock curLock = it.next();
//            lockList.addLock(it.next());
//
//        }
        Lock testlock1 = new Lock("56b3c3d5650066a9ec89cc75", "testLock1", "zO1ZsGJL", "This is a test lock." );
        Lock testlock2 = new Lock("56b3c3d5650066a9ec89cc76", "testLock2", "zO1ZsGJL", "This is another test lock." );
        Lock testlock3 = new Lock("56b3c3d5650066a9ec89cc76", "testLock3", "zO1ZsGJL", "This is another test lock, but it has a longer message.");
        Lock testlock4 = new Lock("56b3c3d5650066a9ec89cc76", "testLock4", "zO1ZsGJL", "This is another test lock, but it has a much longer message. Seriously, this lock's message is pretty long. At least a couple lines." );
        Lock testlock5 = new Lock("56eadde0ce93f260099b8d39", "unlockTestLock5", "zO1ZsGJL");
        Lock testlock6 = new Lock("56b3c3d5650066a9ec89cc76", "testLock6", "zO1ZsGJL", "This is another test lock" );
        Lock testlock7 = new Lock("56b3c3d5650066a9ec89cc76", "testLock7", "zO1ZsGJL", "This is another test lock" );
        Lock testlock8 = new Lock("56b3c3d5650066a9ec89cc76", "testLock8", "zO1ZsGJL", "This is another test lock" );
        Lock testlock9 = new Lock("56b3c3d5650066a9ec89cc76", "testLock9", "zO1ZsGJL", "This is another test lock" );
        Lock testlock10 = new Lock("56b3c3d5650066a9ec89cc76", "testLock10", "zO1ZsGJL", "This is another test lock" );
        Lock testlock11 = new Lock("56b3c3d5650066a9ec89cc76", "testLock11", "zO1ZsGJL", "This is another test lock" );
        Lock testlock12 = new Lock("56b3c3d5650066a9ec89cc76", "testLock12", "zO1ZsGJL", "This is another test lock" );
        lockList.addLock(testlock1);
        lockList.addLock(testlock2);
        lockList.addLock(testlock3);
        lockList.addLock(testlock4);
        lockList.addLock(testlock5);
        lockList.addLock(testlock6);
        lockList.addLock(testlock7);
        lockList.addLock(testlock8);
        lockList.addLock(testlock9);
        lockList.addLock(testlock10);
        lockList.addLock(testlock11);
        lockList.addLock(testlock12);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
