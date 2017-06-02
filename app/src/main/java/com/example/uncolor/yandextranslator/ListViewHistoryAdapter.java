package com.example.uncolor.yandextranslator;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by uncolor on 30.05.17.
 */

public class ListViewHistoryAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    ArrayList<Translate> translations = new ArrayList<Translate>();

    public ListViewHistoryAdapter(Context context, ArrayList<Translate> translations){
        layoutInflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.translations = translations;

    }
    @Override
    public int getCount() {
        return translations.size();
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
        ImageView imageViewFavoriteItem = (ImageView)view.findViewById(R.id.imageViewFavoriteItem);
        if(translations.get(position).isFavorite())
        {
            imageViewFavoriteItem.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.favoriteTintColor));
        }
        else {
            imageViewFavoriteItem.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.tintColor));
        }
        TextView textViewFromItem = (TextView)view.findViewById(R.id.textViewFromItem);
        textViewFromItem.setText(translations.get(position).getTextFrom());
        TextView textViewToItem = (TextView)view.findViewById(R.id.textViewToItem);
        textViewToItem.setText(translations.get(position).getTextTo());
        TextView textViewLangItem = (TextView)view.findViewById(R.id.textViewLangItem);
        textViewLangItem.setText(translations.get(position).getLang());

        return view;
    }
}
