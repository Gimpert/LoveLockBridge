package com.example.owner.lovelockclient;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.example.owner.BridgeCommunication.ServerRelay;

/**
 * Created by Owner on 3/22/2016.
 */
public class BridgeProximity {
    private boolean bridgeProximity;
    private Location currentlocation;

    private static BridgeProximity instance = null;

    public static BridgeProximity getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return instance = new BridgeProximity();
        }
    }


    public BridgeProximity(){
        this.bridgeProximity = false;
        this.currentlocation = null;
    }

    public Location getCurrentlocation() {
        return currentlocation;
    }

    public void setCurrentlocation(Location currentlocation){
        this.currentlocation = currentlocation;
}

    public boolean isBridgeProximity() {
        return bridgeProximity;
    }

    public void setBridgeProximity(boolean bridgeProximity){
        this.bridgeProximity = bridgeProximity;
    }
}
