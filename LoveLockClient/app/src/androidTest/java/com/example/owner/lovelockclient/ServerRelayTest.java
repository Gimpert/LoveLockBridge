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

    public ServerRelayTest() {
        super(Application.class);
    }

//    protected void setUp() throws Exception {
//        super.setUp();
//        createApplication();
//        app = getApplication();
//    }
//
//    public void testSendGetToServer() {
//        ServerRelay serverRelay = new ServerRelay();
//        String resp = serverRelay.sendGetToServer("/locks?name=testLock");
//        assertEquals("{\"_id\":\"56b3c3d5650066a9ec89cc75\",\"name\":\"testLock\",\"message\":\"This is a test lock\"}\r", resp);
//    }
//
//    public void testUnlockLock() {
//        ServerRelay serverRelay = new ServerRelay();
//        String resp = serverRelay.unlockLock("56eadde0ce93f260099b8d39", "10.0", "10.0", "zO1ZsGJL");
//        assertEquals("password validate", resp);
//    }
//
//    public void testCreateLock() {
//        ServerRelay serverRelay = new ServerRelay();
//        //lat=10.0&lng=10.0&message=addlock%20method&name=testAddLock
//        String resp = serverRelay.addLock("testAddLock", "10.0", "10.0", "addLock");
//        //assertEquals("{\"id\":\"56eae2eade1056100d8c1379\",\"name\":\"testAddLock\",\"password\":\"KyZdEh87\"}", resp);
//        assertTrue(resp.contains("\"name\":\"testAddLock\",\""));
//    }
}
