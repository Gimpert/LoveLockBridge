package com.example.owner.lovelockclient;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.example.owner.BridgeCommunication.ServerRelay;

/**
 * Created by Owner on 3/22/2016.
 */
public class BridgeProximityListener implements LocationListener {
    private boolean bridgeProximity ;
    private Location currentlocation;
    public BridgeProximityListener(Boolean bridgeProximity, Location initialLocation){
        this.bridgeProximity = bridgeProximity;
        this.currentlocation = initialLocation;
    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(MainActivity.getContext(),"Gps Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.getContext(),"Gps Disabled", Toast.LENGTH_SHORT).show();
    }

    public Location getCurrentlocation() {
        return currentlocation;
    }

    public boolean isBridgeProximity() {
        return bridgeProximity;
    }
}
