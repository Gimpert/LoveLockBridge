package com.example.owner.lovelockclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.owner.bridgecommunication.ServerRelay;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public static boolean DEBUG = false; //TODO: remove all debug functionality for final product
    private static Context CONTEXT;

    ExpandableListAdapter listAdapter;
    ExpandableListView expKeyListView;
    List<String> listDataHeader;
    HashMap<String, Lock> listDataChild;

    EditText etResponse;
    ServerRelay serverRelay;
    LockList lockList;
    //String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etResponse = (EditText) findViewById(R.id.etResponse);
        serverRelay = new ServerRelay();
        new HttpAsyncTask().execute(serverRelay.getURL());


        expKeyListView = (ExpandableListView) findViewById(R.id.keys_list);

        lockList = new LockList();
        lockList.loadLocks();

        prepareListData();
        listAdapter = new KeyListAdapter(this, listDataHeader, listDataChild);



        expKeyListView.setAdapter(listAdapter);
        expKeyListView.setOnGroupClickListener(null);

    }

    public static Context getContext() {
        return CONTEXT;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return serverRelay.sendToServer(serverRelay.getURL());

        }
        protected void onPostExecute(String result){
            etResponse.setText(result);
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, Lock>();

        //Lock testlock1 = new Lock("56b3c3d5650066a9ec89cc75", "testLock", "This is test lock" );
        //List<String> test = new ArrayList<String>();
        //test.add(testlock1.getId());
        Iterator<Lock> it = lockList.getList().iterator();
        while (it.hasNext()) {
            Lock curLock = it.next();
            listDataHeader.add(curLock.getName());
            listDataChild.put(curLock.getName(), curLock);
        }
        //listDataHeader.add(testlock1.getName());
        //listDataChild.put(listDataHeader.get(0), testlock1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
