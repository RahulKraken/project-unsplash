package com.kraken.project_unsplash.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.kraken.project_unsplash.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);
    }
}