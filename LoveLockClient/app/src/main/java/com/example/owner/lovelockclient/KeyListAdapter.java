package com.example.owner.lovelockclient;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class KeyListAdapter extends BaseAdapter {

    private Context context;
    private LockList listData;

    public KeyListAdapter(Context context, LockList listData) {
        this.context = context;
        this.listData = listData;
    }


    @Override
    public int getCount() {
        return listData.getCount();
    }

    @Override
    public Object getItem(int position) {
        return listData.getLock(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null){
            view = inflater.inflate(R.layout.key_list_group_item, parent);
        }

        TextView keyName = (TextView) view.findViewById(R.id.key_name);
        keyName.setText(listData.getLock(position).getName());

        return view;
    }

}
