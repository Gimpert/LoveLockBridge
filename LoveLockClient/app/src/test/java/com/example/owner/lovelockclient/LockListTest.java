package com.example.owner.lovelockclient;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Created by Joseph Gregory on 2/23/2016.
 */
public class LockListTest {
    @Test
    public void loadLocksTest() {
        MainActivity.DEBUG = true;

        LockList lockList = new LockList();
        LockList.DEBUG_STORED_LOCKS_FILENAME = "app/src/main/res/testLocks";
        lockList.loadLocks();
        ArrayList<Lock> list = lockList.getList();

        assertEquals(list.get(0).getId(), "324k");
        assertEquals(list.get(0).getName(), "Lock 1");
        assertEquals(list.get(0).getMessage(), "This is the secret message");

        assertEquals(list.get(0).getId(), "23%#@#");
        assertEquals(list.get(0).getName(), "Lock 1");
        assertEquals(list.get(0).getMessage(), "secret message of lock 2");
    }
    @Test
    public void saveLocksTest() {

    }
}
