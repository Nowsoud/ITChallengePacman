package com.soft.cyberforest.itchallenge;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HTPActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htp);

        findViewById(R.id.activity_htp).setBackgroundColor(Color.BLACK);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/pixelfont2.ttf");
        ((TextView)findViewById(R.id.textView2)).setTypeface(tf);

        ((TextView)findViewById(R.id.textView3)).setTypeface(tf);

        ((TextView)findViewById(R.id.textView4)).setTypeface(tf);

        ((TextView)findViewById(R.id.textView5)).setTypeface(tf);

    }
}
