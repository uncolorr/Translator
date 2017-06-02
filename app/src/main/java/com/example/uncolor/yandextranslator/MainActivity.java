package com.example.uncolor.yandextranslator;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity implements FragmentEventListener {


    FragmentTransaction transaction = null;
    ArrayList<Translate> translates = new ArrayList<Translate>();
    String xml = new String();
    String TAG = "TAG";

    Button buttonLanguageFrom;
    Button buttonLanguageTo;
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
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.replace(R.id.constraintLayoutMain, translatorFragment);
                    transaction.commit();
                    return true;

                case R.id.navigation_history:
                    historyFragment.getArguments().putParcelableArrayList("translations", translates);
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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

        new LanguageProgressTask().execute("https://translate.yandex.net/api/v1.5/tr/getLangs?key=trnsl.1.1.20170524T080156Z.3a426a0c4a570ecb.995aa3b1f59e3a476b451b1fd5352f710c9660a0&ui=en");

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.constraintLayoutMain, TranslatorFragment.newInstance());
        transaction.commit();


        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.action_bar_layout, null);

        getSupportActionBar().setCustomView(v);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#696969")));

        buttonLanguageFrom = (Button) findViewById(R.id.buttonLanguageFrom);
        buttonLanguageFrom.setText(languageFrom);
        buttonLanguageTo = (Button) findViewById(R.id.buttonLanguageTo);
        buttonLanguageTo.setText(languageTo);


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
        intent.putExtra("lang", xml);
        startActivityForResult(intent, 1);
    }

    public void onClickButtonLanguageTo(View view) {
        Intent intent = new Intent(MainActivity.this, ChooseLanguage.class);
        intent.putExtra("language", languageTo);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    buttonLanguageFrom.setText(data.getStringExtra("language"));
                    break;
                case 2:
                    buttonLanguageTo.setText(data.getStringExtra("language"));
                    break;
            }
        }
    }

    private class ProgressTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... path) {

            String content = new String();
            try {
                content = getContent(path[0]);
            } catch (IOException ex) {
                content = ex.getMessage();
            }

            return content;
        }

        @Override
        protected void onPostExecute(String content) {



            xml = content;
            Log.d(TAG,content);
            Log.d(TAG,content);
            Log.d(TAG,content);
            Log.d(TAG,content);


          /*  XmlPullParserFactory xmlPullParserFactory;
            try {
                Translate translate = new Translate();
                xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(new StringReader(content));
                translate = parseLanguagesXML(xmlPullParser);
                //translate.setTextFrom(editTextFrom.getText().toString());
               // textViewTranslate.setText(translate.getTextTo());
               // changeViewImageButtons(true);


            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/


        }

        private String getContent(String path) throws IOException {
            BufferedReader reader = null;
            try {
                URL url = new URL(path);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(10000);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return (buf.toString());
            } finally {

                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    private class LanguageProgressTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... path) {

            String content = new String();
            try {
                content = getContent(path[0]);
            } catch (IOException ex) {
                content = ex.getMessage();
            }

            return content;
        }

        @Override
        protected void onPostExecute(String content) {

            XmlPullParserFactory xmlPullParserFactory;
            try {
                Translate translate = new Translate();
                xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(new StringReader(content));
                translate = parseLanguagesXML(xmlPullParser);
                //translate.setTextFrom(editTextFrom.getText().toString());
               // textViewTranslate.setText(translate.getTextTo());



            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        private String getContent(String path) throws IOException {
            BufferedReader reader = null;
            try {
                URL url = new URL(path);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(10000);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return (buf.toString());
            } finally {

                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    private Translate parseLanguagesXML(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {

        Translate translate = null;
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = new String();
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    translate = new Translate();
                    break;
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if (tagName.equals("Translation")) {

                        translate.setLang(xmlPullParser.getAttributeValue(null, "lang"));
                    } else if (translate != null) {

                        if (tagName.equals("text")) {
                            translate.setTextTo(xmlPullParser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    return translate;
            }
            eventType = xmlPullParser.next();
        }
        return translate;
    }
}



