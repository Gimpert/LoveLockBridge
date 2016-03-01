package com.example.owner.lovelockclient;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Owner on 2/16/2016.
 */
public class KeyListAdapter extends ArrayAdapter<Lock> {




    public KeyListAdapter(Context context, ArrayList<Lock> listData) {
        super(context, R.layout.key_list_group_item, listData);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Lock lock = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (view == null){
            view = inflater.inflate(R.layout.key_list_group_item, parent, false);
            view.setMinimumHeight(R.dimen.list_item_unexpanded);

        }

        TextView keyName = (TextView) view.findViewById(R.id.key_name);
        keyName.setText(lock.getName());

        return view;
    }

}
