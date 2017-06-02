package com.example.uncolor.yandextranslator;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class FullScreenTranslation extends AppCompatActivity {

    String text = new String();
    TextView textViewTextFullScreen;
    ImageButton imageButtonCloseFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_translation);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        text = getIntent().getStringExtra("text");

        textViewTextFullScreen = (TextView)findViewById(R.id.textViewTextFullScreen);
        textViewTextFullScreen.setMovementMethod(new ScrollingMovementMethod());
        textViewTextFullScreen.setText(text);
        imageButtonCloseFullScreen = (ImageButton)findViewById(R.id.imageButtonCloseFullScreen);

        imageButtonCloseFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
