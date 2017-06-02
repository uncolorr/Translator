package com.example.uncolor.yandextranslator;


import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {


    ListView listViewHistory;
    ArrayList<Translate> translations = new ArrayList<Translate>();


    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translations = getArguments().getParcelableArrayList("translations");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.history_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewHistory = (ListView)view.findViewById(R.id.listViewHistory);
        ListViewHistoryAdapter listViewHistoryAdapter = new ListViewHistoryAdapter(view.getContext(), translations);
        listViewHistory.setAdapter(listViewHistoryAdapter);

    }

    public void setTranslations(ArrayList<Translate> translations){
        this.translations = translations;
    }

}
