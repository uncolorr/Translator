package com.example.uncolor.yandextranslator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by uncolor on 30.05.17.
 */

public class ListViewHistoryAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    public ListViewHistoryAdapter(Context context){
        layoutInflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null)
        {
            view = layoutInflater.inflate(R.layout.listview_history_item, parent, false);
        }
        return view;
    }
}
