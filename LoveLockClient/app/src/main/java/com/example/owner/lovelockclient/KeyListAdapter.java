package com.example.owner.lovelockclient;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
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

import com.example.owner.BridgeCommunication.ServerRelay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Owner on 2/16/2016.
 */
public class KeyListAdapter extends ArrayAdapter<Lock> {
    ServerRelay serverRelay;


    private ArrayList<Lock> listData;
    public KeyListAdapter(Context context, ArrayList<Lock> listData) {
        super(context, R.layout.key_list_group_item, listData);
        this.listData = listData;
        serverRelay = new ServerRelay();

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

        unlock_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow popupWindow = new PopupWindow(inflater.inflate(R.layout.message_popup, null, false), 1, 1, true);
                TextView message_name = (TextView) popupWindow.getContentView().findViewById(R.id.message_name);
                TextView message_body = (TextView) popupWindow.getContentView().findViewById(R.id.message_body);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                message_name.setText(lock.getName());
                if(lock.getMessage() != null) {
                    message_body.setText(lock.getMessage());

                    popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
                }else {
                    new HttpAsyncTask().execute(serverRelay.getURL());
                    popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
                }
            }
        });


        return view;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return serverRelay.sendGetToServer("");

        }
        protected void onPostExecute(String result){

        }
    }
}
