package com.example.owner.BridgeCommunication;

/**
 * Created by Joseph Gregory on 2/15/2016.
 */
public class MessageBuilder {

    public static void unlockLock(ServerRelay relay, String latitude, String longitude, String lockId, String lockName) {
        String urlParams = "/openLock?name=" + lockName + "&lat=" + latitude + "&lng=" + longitude + "&pswd=" + lockId;
        relay.sendGetToServer(urlParams);
    }

    public static void createLock(String latitude, String longitude, String lockId, String lockName, String lockMessage) {

    }

    public static void sendLock(String latitude, String longitude, String lockId, String lockName, String recipientEmailAddress, String sendString) {

    }
}
