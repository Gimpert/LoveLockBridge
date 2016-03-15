package com.example.owner.lovelockclient;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class LockListTest extends ApplicationTestCase<Application> {
    private Application app;

    public LockListTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        app = getApplication();
    }

//    public void loadLocksTest() {
//        createApplication();
//
//        LockList lockList = new LockList();
//        //LockList.DEBUG_STORED_LOCKS_FILENAME = "app/src/main/res/testLocks";
//        lockList.loadLocks();
//        ArrayList<Lock> list = lockList.getList();
//
//        assertEquals(list.get(0).getId(), "324k");
//        assertEquals(list.get(0).getName(), "Lock 1");
//        assertEquals(list.get(0).getMessage(), "This is the secret message");
//
//        assertEquals(list.get(0).getId(), "23%#@#");
//        assertEquals(list.get(0).getName(), "Lock 1");
//        assertEquals(list.get(0).getMessage(), "secret message of lock 2");
//    }

    public void testSaveLocks() {
        LockList lockList = new LockList();
        lockList.addLock(new Lock("324k","Lock 1","This is the secret message"));
        lockList.addLock(new Lock("23%#@#","Lock 2","secret message of lock 2"));
        lockList.saveLocks();

        lockList.loadLocks();
        ArrayList<Lock> list = lockList.getList();

        assertEquals(list.get(0).getId(), "324k");
        assertEquals(list.get(0).getName(), "Lock 1");
        assertEquals(list.get(0).getMessage(), "This is the secret message");

        assertEquals(list.get(1).getId(), "23%#@#");
        assertEquals(list.get(1).getName(), "Lock 2");
        assertEquals(list.get(1).getMessage(), "secret message of lock 2");
    }
}
