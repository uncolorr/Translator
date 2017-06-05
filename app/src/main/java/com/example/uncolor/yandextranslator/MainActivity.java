package com.example.uncolor.yandextranslator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements FragmentEventListener {

    private static final int REQUEST_CODE_FROM = 1;
    private static final int REQUEST_CODE_TO = 2;

    LayoutInflater inflater;
    View actionBarTranslator;
    View actionBarHistory;

    FragmentTransaction transaction = null;
    ArrayList<Translate> translates = new ArrayList<Translate>();
    String xml = new String();
    String TAG = "MyLogs";
    Map <String, String> langsMap = new HashMap<String, String>();
    ArrayList<String> arrayListLangs = new ArrayList<String>();
    String xmlLanguages = new String();



    Button buttonLanguageFrom;
    Button buttonLanguageTo;
    ImageButton imageButtonSwap;
    ImageButton imageButtonClearHistory;
    TranslatorFragment translatorFragment = TranslatorFragment.newInstance();
    HistoryFragment historyFragment = HistoryFragment.newInstance();
    String languageFrom = new String("Английский");
    String languageTo = new String("Русский");

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_translator:
                    getSupportActionBar().setCustomView(actionBarTranslator);
                    translatorFragment.getArguments().putString("langKeyFrom", getKeyToValue(buttonLanguageFrom.getText().toString()));
                    translatorFragment.getArguments().putString("langKeyTo", getKeyToValue(buttonLanguageTo.getText().toString()));
                    transaction = getSupportFragmentManager().beginTransaction();
                   // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.replace(R.id.constraintLayoutMain, translatorFragment);
                    transaction.commit();
                    return true;

                case R.id.navigation_history:
                    getSupportActionBar().setCustomView(actionBarHistory);
                    historyFragment.getArguments().putParcelableArrayList("translations", translates);
                    transaction = getSupportFragmentManager().beginTransaction();
                    //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.replace(R.id.constraintLayoutMain, historyFragment);
                    transaction.commit();
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        actionBarTranslator = inflater.inflate(R.layout.action_bar_layout, null);
        actionBarHistory = inflater.inflate(R.layout.action_bar_history, null);


        getSupportActionBar().setCustomView(actionBarTranslator);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#696969")));

        buttonLanguageFrom = (Button) findViewById(R.id.buttonLanguageFrom);
        buttonLanguageFrom.setText(languageFrom);
        buttonLanguageTo = (Button) findViewById(R.id.buttonLanguageTo);
        buttonLanguageTo.setText(languageTo);
        imageButtonClearHistory = (ImageButton)findViewById(R.id.imageButtonClearHistory);


        try
        {
            Resources res = getResources();
            InputStream inputStream = res.openRawResource(R.raw.languages);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            xmlLanguages = new String(b);

        } catch (Exception e) {
            e.printStackTrace();
        }

        XmlPullParserFactory xmlPullParserFactory;
        try {

            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlLanguages));
            langsMap = parseLanguagesXML(xmlPullParser);

            for(String key: langsMap.keySet()){
                arrayListLangs.add(langsMap.get(key));
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        translatorFragment.getArguments().putString("langKeyFrom", getKeyToValue(buttonLanguageFrom.getText().toString()));
        translatorFragment.getArguments().putString("langKeyTo", getKeyToValue(buttonLanguageTo.getText().toString()));
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.constraintLayoutMain, translatorFragment);
        transaction.commit();


    }
    
    @Override
    public void onResume(){
        super.onResume();

        getSupportActionBar().setCustomView(actionBarTranslator);
        translatorFragment.getArguments().putString("langKeyFrom", getKeyToValue(buttonLanguageFrom.getText().toString()));
        translatorFragment.getArguments().putString("langKeyTo", getKeyToValue(buttonLanguageTo.getText().toString()));
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.remove(translatorFragment);
        transaction.replace(R.id.constraintLayoutMain, translatorFragment);
       // transaction.commit();
    }


    @Override
    public void setArrayListHistory(ArrayList<Translate> translates) {
        for (int i = 0; i < translates.size(); i++) {
            this.translates.add(0, translates.get(i));
        }

    }

    public void onClickButtonLanguageFrom(View view) {
        Intent intent = new Intent(MainActivity.this, ChooseLanguage.class);
        intent.putExtra("language", languageFrom);
        intent.putStringArrayListExtra("languages", arrayListLangs);
        startActivityForResult(intent, REQUEST_CODE_FROM);
    }

    public void onClickButtonLanguageTo(View view) {
        Intent intent = new Intent(MainActivity.this, ChooseLanguage.class);
        intent.putExtra("language", languageTo);
        intent.putStringArrayListExtra("languages", arrayListLangs);
        startActivityForResult(intent, REQUEST_CODE_TO);
    }

    public void onClickImageButtonSwap(View view){
        String temp = new String(buttonLanguageFrom.getText().toString());
        buttonLanguageFrom.setText(buttonLanguageTo.getText().toString());
        buttonLanguageTo.setText(temp);

        translatorFragment.getArguments().putString("langKeyFrom", getKeyToValue(buttonLanguageFrom.getText().toString()));
        translatorFragment.getArguments().putString("langKeyTo", getKeyToValue(buttonLanguageTo.getText().toString()));
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.remove(translatorFragment);
        transaction.replace(R.id.constraintLayoutMain, translatorFragment);
        transaction.commit();
    }

    public void onClickImageButtonClearHistory(View view){
        historyFragment.clearHistory();
        translates.clear();
        historyFragment.getArguments().putParcelableArrayList("translations", translates);
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.remove(historyFragment);
        transaction.replace(R.id.constraintLayoutMain, historyFragment);
        transaction.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FROM:
                    buttonLanguageFrom.setText(data.getStringExtra("language"));
                    break;
                case REQUEST_CODE_TO:
                    buttonLanguageTo.setText(data.getStringExtra("language"));
                    break;
            }

        }
    }


    private Map<String, String> parseLanguagesXML(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {

        Map<String, String> langsMap = new HashMap<String, String>();
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = new String();
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if (tagName.equals("Item")) {

                        String key = new String(xmlPullParser.getAttributeValue(null, "key"));
                        String value = new String(xmlPullParser.getAttributeValue(null, "value"));
                        langsMap.put(key,value);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = xmlPullParser.next();
        }
        return langsMap;
    }

    private String getKeyToValue(String value){
        for(String key: langsMap.keySet()){
            if(Objects.equals(langsMap.get(key), value)){
                return key;
            }
        }
        return null;
    }
}



