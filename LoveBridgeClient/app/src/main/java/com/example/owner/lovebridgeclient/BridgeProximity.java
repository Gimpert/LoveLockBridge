package com.example.owner.lovebridgeclient;

import android.location.Location;

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
