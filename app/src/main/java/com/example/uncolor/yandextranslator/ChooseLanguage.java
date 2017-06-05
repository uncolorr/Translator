package com.example.uncolor.yandextranslator;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ChooseLanguage extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayListLanguages = new ArrayList<String>();

    String currentLanguage = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#696969")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Выбор языка");

        arrayListLanguages = getIntent().getExtras().getStringArrayList("languages");
        listView = (ListView)findViewById(R.id.listViewLanguages);
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListLanguages);
        listView.setAdapter(languagesAdapter);

        listView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentLanguage = arrayListLanguages.get(position);
                Intent intent = new Intent();
                intent.putExtra("language", currentLanguage);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
