package com.example.owner.lovelockclient;

import android.app.Activity;
import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;

import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class LockListTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Application app;
    private MainActivity act;

    public LockListTest() { super(MainActivity.class); }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testloadLocksAfterPauseTest() {
        LockList lockList = LockList.getInstance();
        lockList.addLock(new Lock("324k", "Lock 1", "pass 1"));
        lockList.addLock(new Lock("23%#@#", "Lock 2", "password2"));
        //lockList.saveLocks();

        Activity activity = getActivity();
        activity.finish();

        lockList.loadLocks();
        LockList.getInstance().addLock(new Lock("09ykk", "Lock 3", "password#3"));

        ArrayList<Lock> list = lockList.getList();

        assertEquals(list.get(0).getId(), "324k");
        assertEquals(list.get(0).getName(), "Lock 1");
        assertEquals(list.get(0).getPassword(), "pass 1");

        assertEquals(list.get(1).getId(), "23%#@#");
        assertEquals(list.get(1).getName(), "Lock 2");
        assertEquals(list.get(1).getPassword(), "password2");

        assertEquals(list.get(list.size() - 1).getId(), "09ykk");
        assertEquals(list.get(list.size() - 1).getName(), "Lock 3");
        assertEquals(list.get(list.size() - 1).getPassword(), "password#3");
    }

    public void testSaveLocks() {
        LockList lockList = LockList.getInstance();
        lockList.addLock(new Lock("324k","Lock 1","pass 1"));
        lockList.addLock(new Lock("23%#@#","Lock 2","password2"));
        lockList.saveLocks();

        lockList.loadLocks();
        ArrayList<Lock> list = lockList.getList();

        assertEquals(list.get(0).getId(), "324k");
        assertEquals(list.get(0).getName(), "Lock 1");
        assertEquals(list.get(0).getPassword(), "pass 1");

        assertEquals(list.get(1).getId(), "23%#@#");
        assertEquals(list.get(1).getName(), "Lock 2");
        assertEquals(list.get(1).getPassword(), "password2");
    }
}
