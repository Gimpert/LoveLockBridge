package com.example.owner.lovelockclient;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.example.owner.BridgeCommunication.ServerRelay;

/**
 * Created by Joseph Gregory on 3/15/2016.
 */
public class ServerRelayTest extends ApplicationTestCase<Application> {
    private Application app;
    ServerRelay serverRelay;

    public ServerRelayTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        app = getApplication();

    }

//    public void testSendGetToServer() {
//        ServerRelay serverRelay = new ServerRelay();
//        String resp = serverRelay.sendGetToServer("/locks?name=testLock");
//        assertEquals("{\"_id\":\"56b3c3d5650066a9ec89cc75\",\"name\":\"testLock\",\"message\":\"This is a test lock\"}\r", resp);
//    }

    public void testUnlockLock() {
        serverRelay = new ServerRelay();
        String resp = serverRelay.unlockLock("56eadde0ce93f260099b8d39", "10.0", "10.0", "zO1ZsGJL");
        assertEquals("{\"message\":\"password validate\"}\r", resp);
    }

    public void testAddLock() {
        serverRelay = new ServerRelay();
        //lat=10.0&lng=10.0&message=addlock%20method&name=testAddLock
        String resp = serverRelay.addLock("testAddLock", "10.0", "10.0", "addLock");
        //assertEquals("{\"id\":\"5[71675ac22e79914098d7185\",\"name\":\"testAddLock\",\"password\":\"U6RfPoud\"}", resp);
        assertTrue(resp.contains("\"name\":\"testAddLock\",\""));
        assertTrue(resp.contains("{\"id\":"));
        assertTrue(resp.contains("password\":"));
    }

    public void testSendKey() {
        serverRelay = new ServerRelay();
        String resp = serverRelay.sendKey("56eadde0ce93f260099b8d39","zO1ZsGJL", "jcgrego3@ncsu.edu", "Joe Biden", "President Obama", "Here is the key to my heart");
        assertTrue(resp.toLowerCase().equals("message sent"));
    }

    public void testIsInBridgeRange() {
        serverRelay = new ServerRelay();
        boolean resp = serverRelay.isInBridgeRange("10.0", "10.0");
        assertTrue(resp);
    }
}
