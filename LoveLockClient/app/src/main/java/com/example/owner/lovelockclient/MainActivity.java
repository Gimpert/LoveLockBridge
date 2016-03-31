package com.example.owner.lovelockclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.example.owner.BridgeCommunication.ServerRelay;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static Context CONTEXT;

    KeyListAdapter listAdapter;
    ListView keyListView;

    TextView etResponse;
    ServerRelay serverRelay;
    LockList lockList;
    //String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etResponse = (TextView) findViewById(R.id.etResponse);
        serverRelay = new ServerRelay();
        new HttpAsyncTask().execute(serverRelay.getURL());


        keyListView = (ListView) findViewById(R.id.key_list_view);


        lockList = LockList.getInstance();
        lockList.loadLocks();
        prepareListData();





        //prepareListData();
        listAdapter = new KeyListAdapter(this, lockList.getList());



        keyListView.setAdapter(listAdapter);
        keyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String testToast = ((Lock) keyListView.getItemAtPosition(position)).getName();
                Toast.makeText(MainActivity.this, testToast, Toast.LENGTH_SHORT).show();
            }
        });



    }

    public static Context getContext() {
        return CONTEXT;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return serverRelay.sendGetToServer(serverRelay.getURL());

        }
        protected void onPostExecute(String result){
            etResponse.setText(result);
        }
    }

    public void prepareListData() {

//        Iterator<Lock> it = lockList.getList().iterator();
//        while (it.hasNext()) {
//            Lock curLock = it.next();
//            lockList.addLock(it.next());
//
//        }
        Lock testlock1 = new Lock("56eadde0ce93f260099b8d39", "testLock", "zO1ZsGJL" );
        Lock testlock2 = new Lock("56b3c3d5650066a9ec89cc76", "testLock2", "This is another test lock." );
        Lock testlock3 = new Lock("56b3c3d5650066a9ec89cc76", "testLock3", "This is another test lock, but it has a longer message.");
        Lock testlock4 = new Lock("56b3c3d5650066a9ec89cc76", "testLock4", "This is another test lock, but it has a much longer message. Seriously, this lock's message is pretty long. At least a couple lines." );
        Lock testlock5 = new Lock("56b3c3d5650066a9ec89cc76", "testLock5");
        Lock testlock6 = new Lock("56b3c3d5650066a9ec89cc76", "testLock6", "This is another test lock" );
        Lock testlock7 = new Lock("56b3c3d5650066a9ec89cc76", "testLock7", "This is another test lock" );
        Lock testlock8 = new Lock("56b3c3d5650066a9ec89cc76", "testLock8", "This is another test lock" );
        Lock testlock9 = new Lock("56b3c3d5650066a9ec89cc76", "testLock9", "This is another test lock" );
        Lock testlock10 = new Lock("56b3c3d5650066a9ec89cc76", "testLock10", "This is another test lock" );
        Lock testlock11 = new Lock("56b3c3d5650066a9ec89cc76", "testLock11", "This is another test lock" );
        Lock testlock12 = new Lock("56b3c3d5650066a9ec89cc76", "testLock12", "This is another test lock" );
        lockList.addLock(testlock1);
        lockList.addLock(testlock2);
        lockList.addLock(testlock3);
        lockList.addLock(testlock4);
        lockList.addLock(testlock5);
        lockList.addLock(testlock6);
        lockList.addLock(testlock7);
        lockList.addLock(testlock8);
        lockList.addLock(testlock9);
        lockList.addLock(testlock10);
        lockList.addLock(testlock11);
        lockList.addLock(testlock12);


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
