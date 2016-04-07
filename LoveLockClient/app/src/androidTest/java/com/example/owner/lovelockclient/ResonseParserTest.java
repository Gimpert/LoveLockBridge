package com.example.owner.lovelockclient;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.owner.BridgeCommunication.ResponseParser;

/**
 * Created by Owner on 3/28/2016.
 */
public class ResonseParserTest extends ApplicationTestCase<Application>{

    private Application app;
    private ResponseParser r;

    public ResonseParserTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        app = getApplication();
        r = new ResponseParser();
    }

    public void testParseMessage() {
        String result = r.parseMessage("{\"message\":\"password validate\"}\r");
        assertEquals("password validate", result);

        result = r.parseMessage("{\"message\":\"!@#$#@%%@$@$#gfdsfgsdfgfsdv345   453 23$%#$\"}\r");
        assertEquals("!@#$#@%%@$@$#gfdsfgsdfgfsdv345   453 23$%#$", result);
    }

    public void testAddResponse() {
        String[] result = r.parseAddResponse("{\"id\":\"5[706a5fb45cab4280f27522f\",\"name\":\"testAddLock\",\"password\":\"7mP8GM19\"}");
        assertEquals("5[706a5fb45cab4280f27522f", result[0]);
        assertEquals("7mP8GM19", result[1]);
    }
}
