package com.kraken.project_unsplash.Fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;

import com.kraken.project_unsplash.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private static final String TAG = "PreferenceFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(androidx.preference.Preference preference) {
        switch (preference.getKey()) {
            case "pref_theme":
                Log.d(TAG, "onPreferenceTreeClick: CHANGE THEME");
                return true;
            case "pref_layout":
                Log.d(TAG, "onPreferenceTreeClick: CHANGE LAYOUT");
                return true;
            case "pref_load_quality":
                Log.d(TAG, "onPreferenceTreeClick: LOAD QUALITY");
                return true;
            case "pref_download_quality":
                Log.d(TAG, "onPreferenceTreeClick: DOWNLOAD QUALITY");
                return true;
            case "pref_wallpaper_quality":
                Log.d(TAG, "onPreferenceTreeClick: WALLPAPER QUALITY");
                return true;
            case "pref_version":
                Log.d(TAG, "onPreferenceTreeClick: VERSION");
                return true;
            case "pref_intro":
                Log.d(TAG, "onPreferenceTreeClick: INTRODUCTION");
                return true;
            case "pref_github":
                Log.d(TAG, "onPreferenceTreeClick: GITHUB");
                return true;
            case "pref_report_bugs":
                Log.d(TAG, "onPreferenceTreeClick: REPORT BUGS");
                return true;
            case "pref_privacy_policy":
                Log.d(TAG, "onPreferenceTreeClick: PRIVACY POLICY");
                return true;
        }
        return false;
    }
}