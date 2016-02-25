package com.example.owner.lovelockclient;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.owner.BridgeCommunication.ServerRelay;
/**
 * Created by Joseph Gregory on 2/16/2016.
 */
public class ServerRelayTest {
    @Test
    public void sendToServer() throws Exception {
        ServerRelay relay = new ServerRelay();

        String result = relay.sendToServer("testLock");
        assertEquals(4, 2 + 2);
        //Log.d("ServerRelayTest", "TESTTTT");
        //Log.d("ServerRelayTest", result);
    }
}
