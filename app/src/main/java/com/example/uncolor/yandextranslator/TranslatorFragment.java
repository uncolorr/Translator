package com.example.uncolor.yandextranslator;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class TranslatorFragment extends Fragment {

    private TextView textViewTranslate;
    private EditText editTextFrom;
    private ImageButton buttonDeleteText;
    private ImageButton imageButtonShare;
    private ImageButton imageButtonFullScreenTranslate;
    private ImageButton imageButtonFavorite;
    private static final String API_KEY = "trnsl.1.1.20170524T080156Z.3a426a0c4a570ecb.995aa3b1f59e3a476b451b1fd5352f710c9660a0";
    private static final String host = "https://translate.yandex.net/api/v1.5/tr/translate?";



    public static TranslatorFragment newInstance() {
        TranslatorFragment fragment = new TranslatorFragment();
        return fragment;
    }

    private void changeViewImageButtons(boolean isVisible){
        float alpha;
        imageButtonShare.setEnabled(isVisible);
        imageButtonFullScreenTranslate.setEnabled(isVisible);
        imageButtonFavorite.setEnabled(isVisible);
        if(!isVisible)
        {
            alpha = 0.0f;
        }
        else
        {
            alpha = 1.0f;
        }
        imageButtonShare.setAlpha(alpha);
        imageButtonFavorite.setAlpha(alpha);
        imageButtonFullScreenTranslate.setAlpha(alpha);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onClickDeleteTextButton(View view){
        editTextFrom.getText().clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.translator_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);

        editTextFrom = (EditText)view.findViewById(R.id.editTextFrom);
        editTextFrom.setHorizontallyScrolling(false);
        editTextFrom.setMaxLines(10000);
        textViewTranslate = (TextView)view.findViewById(R.id.textViewTranslate);
        textViewTranslate.setMovementMethod(new ScrollingMovementMethod());
        imageButtonFavorite = (ImageButton)view.findViewById(R.id.imageButtonFavorite);
        imageButtonFullScreenTranslate = (ImageButton)view.findViewById(R.id.imageButtonFullScreenTranslate);
        imageButtonShare = (ImageButton)view.findViewById(R.id.imageButtonShare);
        changeViewImageButtons(false);

        imageButtonFullScreenTranslate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),FullScreenTranslation.class);
                intent.putExtra("text", textViewTranslate.getText().toString());
                startActivity(intent);
            }
        });

        imageButtonShare.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = textViewTranslate.getText().toString();
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent,"Поделиться переводом"));
            }
        });


        buttonDeleteText = (ImageButton)view.findViewById(R.id.buttonDeleteText);
        buttonDeleteText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View view){
                editTextFrom.getText().clear();
            }
        });

        editTextFrom.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
                    String lang = "en-ru";
                    String myURL = host + "lang=" + lang + "&key=" + API_KEY + "&text=" + URLEncoder.encode(editTextFrom.getText().toString(), "UTF-8");

                    new ProgressTask().execute(myURL);

                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editTextFrom.getText().toString().isEmpty()){
                    textViewTranslate.setText("");
                    changeViewImageButtons(false);


                }

            }
        });
    }

    private class ProgressTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... path) {

            String content = new String();
            try{
                content = getContent(path[0]);
            }
            catch (IOException ex){
                content = ex.getMessage();
            }

            return content;
        }

        @Override
        protected void onPostExecute(String content) {

            XmlPullParserFactory xmlPullParserFactory;
            try {

                xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(new StringReader(content));

                Translate translate = new Translate("","","");
                translate = parseXML(xmlPullParser);
                translate.setTextFrom(editTextFrom.getText().toString());
                textViewTranslate.setText(translate.getTextTo());
                changeViewImageButtons(true);



            } catch (XmlPullParserException e){
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }


        }

        private String getContent(String path) throws IOException {
            BufferedReader reader = null;
            try {
                URL url=new URL(path);
                HttpsURLConnection connection=(HttpsURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(10000);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.connect();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return(buf.toString());
            }
            finally {

                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    private Translate parseXML(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {

        Translate translate = null;
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT){
            String tagName = new String();
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    translate = new Translate("","","");
                    break;
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if (tagName.equals("Translation")){

                        translate.setLang(xmlPullParser.getAttributeValue(null,"lang"));
                    } else if (translate != null){

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
