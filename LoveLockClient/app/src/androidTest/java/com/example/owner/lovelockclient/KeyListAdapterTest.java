package com.example.owner.lovelockclient;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class KeyListAdapterTest extends ApplicationTestCase<Application> {
    public KeyListAdapterTest() {
        super(Application.class);
    }

//    public void testUnlockButton() {
//
//        ListView keyListView = (ListView) findViewById(R.id.key_list_view);
//
//
//        LockList lockList = LockList.getInstance();
//        lockList.loadLocks();
//
//        //prepareListData
//        Lock testlock1 = new Lock("56eadde0ce93f260099b8d39", "testLock", "zO1ZsGJL" );
//        Lock testlock2 = new Lock("56b3c3d5650066a9ec89cc76", "testLock2", "This is another test lock." );
//        Lock testlock3 = new Lock("56b3c3d5650066a9ec89cc76", "testLock3", "This is another test lock, but it has a longer message.");
//        Lock testlock4 = new Lock("56b3c3d5650066a9ec89cc76", "testLock4", "This is another test lock, but it has a much longer message. Seriously, this lock's message is pretty long. At least a couple lines." );
//        Lock testlock5 = new Lock("56b3c3d5650066a9ec89cc76", "testLock5");
//        Lock testlock6 = new Lock("56b3c3d5650066a9ec89cc76", "testLock6", "This is another test lock" );
//        Lock testlock7 = new Lock("56b3c3d5650066a9ec89cc76", "testLock7", "This is another test lock" );
//        Lock testlock8 = new Lock("56b3c3d5650066a9ec89cc76", "testLock8", "This is another test lock" );
//        Lock testlock9 = new Lock("56b3c3d5650066a9ec89cc76", "testLock9", "This is another test lock" );
//        Lock testlock10 = new Lock("56b3c3d5650066a9ec89cc76", "testLock10", "This is another test lock" );
//        Lock testlock11 = new Lock("56b3c3d5650066a9ec89cc76", "testLock11", "This is another test lock" );
//        Lock testlock12 = new Lock("56b3c3d5650066a9ec89cc76", "testLock12", "This is another test lock" );
//        lockList.addLock(testlock1);
//        lockList.addLock(testlock2);
//        lockList.addLock(testlock3);
//        lockList.addLock(testlock4);
//        lockList.addLock(testlock5);
//        lockList.addLock(testlock6);
//        lockList.addLock(testlock7);
//        lockList.addLock(testlock8);
//        lockList.addLock(testlock9);
//        lockList.addLock(testlock10);
//        lockList.addLock(testlock11);
//        lockList.addLock(testlock12);
//
//
//        KeyListAdapter listAdapter = new KeyListAdapter(getContext(), lockList.getList());
//
//
//
//        keyListView.setAdapter(listAdapter);
//
//
//    }
}