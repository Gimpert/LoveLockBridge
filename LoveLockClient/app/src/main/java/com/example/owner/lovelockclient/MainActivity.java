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
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.owner.BridgeCommunication.ResponseParser;
import com.example.owner.BridgeCommunication.ServerRelay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.InputMismatchException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static Context CONTEXT;
    private static final boolean DEBUG = true;
    private static final String NO_RESPONSE = "No response";
    public static final long TIMEOUT_SECONDS = 4L;

    KeyListAdapter listAdapter;
    ListView keyListView;

    GoogleApiClient locationClient = null;

    private LocationRequest mLocationRequest;

    private static int UPDATE_INTERVAL = 10000,FATEST_INTERVAL = 5000, DISPLACEMENT = 0;
    private static final int PROXIMITY_CHECK = 0, ADD_LOCK = 1, RECEIVE_KEY = 2;
    private static final int REQUEST_LOCATION = 0;

    TextView etResponse;
    LockList lockList;
    //String response;
    Button attachButton;
    Button attachSubmitButton;
    Button receiveButton;
    Button receiveSubmitButton;

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


        final LayoutInflater inflater = LayoutInflater.from(this);

        //Retrieve attach button
        attachButton = (Button) findViewById(R.id.attach_button);
        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View popupView = inflater.inflate(R.layout.add_lock_popup, null, false );
                final PopupWindow popupWindow = new PopupWindow(popupView, 1, 1, true );
                final EditText etLockName = (EditText) popupView.findViewById(R.id.lock_name_form);
                final EditText etLockMessage = (EditText) popupView.findViewById(R.id.lock_message_form);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);


                popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);

                attachSubmitButton = (Button) popupView.findViewById(R.id.add_submit_button);
                attachSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etLockName.getText().toString().equals("") || etLockMessage.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.getContext(),"Fields cannot be blank", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(etLockName.getText(), etLockMessage.getText(), ADD_LOCK);
                        httpAsyncTask.execute();
                        String result = "";
                        try {
                            result = httpAsyncTask.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                        } catch (TimeoutException e) {
                            Toast.makeText(MainActivity.getContext(),"Server Timeout", Toast.LENGTH_SHORT).show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (result != null && result.equals(NO_RESPONSE)) {
                            Toast.makeText(MainActivity.getContext(),"Network Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.getContext(),"Lock Added to Bridge", Toast.LENGTH_SHORT).show();
                        }
                        popupWindow.dismiss();
                    }
                });
            }
        });

        receiveButton = (Button) findViewById(R.id.bridge_button);
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View popupView = inflater.inflate(R.layout.receive_lock_popup, null, false);
                final PopupWindow popupWindow = new PopupWindow(popupView, 1, 1, true);
                final EditText etKeyString = (EditText) popupView.findViewById(R.id.key_string);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
                receiveSubmitButton = (Button) popupView.findViewById(R.id.receive_submit_button);
                receiveSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String[] keyValues = ResponseParser.parseKeyString(etKeyString.getText().toString());
                            Lock lock = new Lock(keyValues[1], keyValues[0], keyValues[2]);
                            LockList.getInstance().addLock(lock);
                            listAdapter.notifyDataSetChanged();
                            popupWindow.dismiss();
                        } catch (InputMismatchException e) {
                            Toast.makeText(MainActivity.this, "Incorrect key string format.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Initialize the Location Service


        new HttpAsyncTask().execute();

        //Initialize the lock list, and load saved locks
        lockList = LockList.getInstance();
        lockList.loadLocks();
        prepareListData();

        //Initialize the listview displaying the user's locks
        keyListView = (ListView) findViewById(R.id.key_list_view);
        listAdapter = new KeyListAdapter(this);

        keyListView.setAdapter(listAdapter);

        keyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String testToast = ((Lock) keyListView.getItemAtPosition(position)).getName();
                Toast.makeText(MainActivity.this, testToast, Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void onPause() {
        super.onPause();
        LockList.getInstance().saveLocks();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION} ,REQUEST_LOCATION);
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

    @Override
    public void onLocationChanged(Location location) {
        BridgeProximity prox = BridgeProximity.getInstance();
        prox.setCurrentlocation(location);
        new HttpAsyncTask().execute();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        String name = null;
        String message = null;
        int taskCode;
        //constructor with paramaters to execute addLock case
        public HttpAsyncTask(Editable name, Editable message, int taskCode) {
            this.name = name.toString();
            this.message = message.toString();
            this.taskCode = taskCode;

        }
        // empty constructor for proximity check case (Default);
        //If the current location is not null, executes a check with the server to see if a bridge
        //is in range
        public HttpAsyncTask(){
            this.taskCode = PROXIMITY_CHECK;
        }

        @Override
        protected String doInBackground(String... params) {
            switch (taskCode) {
                case PROXIMITY_CHECK:
                    Location currentLocation = BridgeProximity.getInstance().getCurrentlocation();
                    boolean inRange = false;
                    if (currentLocation != null) {
                        inRange = ServerRelay.isInBridgeRange("" + currentLocation.getLatitude(), "" + currentLocation.getLongitude());
                    }
                    BridgeProximity.getInstance().setBridgeProximity(inRange);
                    return null;
                case ADD_LOCK:
                    if (name != null && message != null && BridgeProximity.getInstance().getCurrentlocation() != null){
                        Location loc = BridgeProximity.getInstance().getCurrentlocation();
                        String lat = "" + loc.getLatitude();
                        String lng = "" + loc.getLongitude();
                        String response[] = ResponseParser.parseAddResponse(ServerRelay.addLock(name, lat, lng, message));
                        if (response[0] == null || response[1] == null || response[0].equals("") || response[1].equals("")) {
                            return NO_RESPONSE;
                        } else {
                            Lock newLock = new Lock(response[0], name, response[1], message);
                            LockList.getInstance().addLock(newLock);
                        }

                    }
                    return null;
            }
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
        if (DEBUG) {
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
