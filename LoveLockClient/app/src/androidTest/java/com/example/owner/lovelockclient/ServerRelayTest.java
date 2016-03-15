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

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        app = getApplication();
    }

    public void testSendToServer() {
        ServerRelay serverRelay = new ServerRelay();
        String resp = serverRelay.sendToServer(serverRelay.getURL());
        assertEquals("{\"_id\":\"56b3c3d5650066a9ec89cc75\",\"name\":\"testLock\",\"message\":\"This is a test lock\"}\r", resp);
    }
}
