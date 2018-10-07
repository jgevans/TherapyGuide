package com.hedgehogproductions.therapyguide.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hedgehogproductions.therapyguide.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        final Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setBackgroundColor(getResources().getColor(R.color.colourIntro));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colourIntroAccent));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colourIntroDark));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
