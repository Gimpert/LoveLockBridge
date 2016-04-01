package com.example.owner.lovelockclient;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.location.Location;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.BridgeCommunication.ResponseParser;
import com.example.owner.BridgeCommunication.ServerRelay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Owner on 2/16/2016.
 */
public class KeyListAdapter extends ArrayAdapter<Lock> {
    static final String NO_LOCK_MESSAGE = "You are not in range of a bridge.";
    static final String NO_LOCATION = "Loction Unknown.";
    ResponseParser responseParser;


    public KeyListAdapter(Context context) {
        super(context, R.layout.key_list_group_item, LockList.getInstance().getList());
        responseParser = new ResponseParser();

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Lock lock = getItem(position);
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        if (view == null){
            view = inflater.inflate(R.layout.key_list_group_item, parent, false);

        }

        TextView keyName = (TextView) view.findViewById(R.id.key_name);
        Button send_button = (Button) view.findViewById(R.id.send_button);
        Button unlock_button = (Button) view.findViewById(R.id.unlock_button);
        Button throw_button = (Button) view.findViewById(R.id.throw_away_button);
        keyName.setText(lock.getName());

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        throw_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockList.getInstance().removeLock(lock);
                LockList.getInstance().getList().remove(lock);
                notifyDataSetChanged();
                Toast.makeText(MainActivity.getContext(), "Removed " + lock.getName(), Toast.LENGTH_SHORT).show();
                //TODO create toast to notify user
            }
        });



        unlock_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow popupWindow = new PopupWindow(inflater.inflate(R.layout.message_popup, null, false), 1, 1, true);
                TextView message_name = (TextView) popupWindow.getContentView().findViewById(R.id.message_name);
                TextView message_body = (TextView) popupWindow.getContentView().findViewById(R.id.message_body);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                message_name.setTextSize(20);
                String headerMessage = "Message from " + lock.getName();
                if (lock.getMessage() != null) {
                    message_name.setText(headerMessage);
                    message_body.setText(lock.getMessage());

                    popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
                } else {
                    Location currentLoction = BridgeProximity.getInstance().getCurrentlocation();
                    if (currentLoction != null) {

                        HttpAsyncTask httpAsyncTask = new HttpAsyncTask("" + currentLoction.getLatitude(), "" + currentLoction.getLongitude(), lock);
                        httpAsyncTask.execute();

                        String message;
                        try {
                            message = responseParser.parseMessage(httpAsyncTask.get());
                            lock.setMessage(message);
                        } catch (Exception e) {
                            message = null;
                        }

                        if (message == null) {
                            message_name.setText(NO_LOCK_MESSAGE);
                            message_body.setText("");
                        } else {
                            message_name.setText(headerMessage);
                            message_body.setText(message);
                        }
                    } else {
                        message_name.setText(NO_LOCATION);
                    }

                    popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
                }
            }
        });


        return view;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private String lat,lng;
        private Lock lock;

        public HttpAsyncTask(String lat, String lng, Lock lock){
            this.lat = lat;
            this.lng = lng;
            this.lock = lock;
        }


        @Override
        protected String doInBackground(String... params) {
            return ServerRelay.unlockLock(lock.getId(), lat, lng, lock.getPassword());

        }
        protected void onPostExecute(String result){
            lock.setMessage(responseParser.parseMessage(result));
        }
    }
}
