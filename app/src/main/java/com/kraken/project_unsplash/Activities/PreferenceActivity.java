package com.kraken.project_unsplash.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.kraken.project_unsplash.Fragments.PreferenceFragment;
import com.kraken.project_unsplash.R;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            TextView tv = findViewById(R.id.toolbar_title);
            tv.setText("Settings");
        }

        // put preference fragment in frame layout
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.preference_container, new PreferenceFragment())
                .commit();
    }
}
