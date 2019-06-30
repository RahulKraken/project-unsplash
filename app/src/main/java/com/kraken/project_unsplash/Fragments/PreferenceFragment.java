package com.kraken.project_unsplash.Fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private static final String TAG = "PreferenceFragment";
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);
        if (getActivity() != null) sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case "pref_theme":
                changeThemeValue(preference.getKey());
                return true;
            case "pref_layout":
                changeLayoutValue(preference.getKey());
                return true;
            case "pref_load_quality":
                changeLoadQuality(preference.getKey());
                return true;
            case "pref_download_quality":
                changeDownloadQuality(preference.getKey());
                return true;
            case "pref_wallpaper_quality":
                changeWallpaperQuality(preference.getKey());
                return true;
            case "pref_version":
                handleVersion();
                return true;
            case "pref_intro":
                handleIntro();
                return true;
            case "pref_github":
                handleGithub();
                return true;
            case "pref_report_bugs":
                reportBugs();
                return true;
            case "pref_privacy_policy":
                showPrivacyPolicy();
                return true;
        }
        return false;
    }

    private void changeThemeValue(String key) {
        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);
            adapter.addAll(Constants.THEME_OPTIONS);
            createDialog("Theme", key, adapter);
        }
    }

    private void changeLayoutValue(String key) {
        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);
            adapter.addAll(Constants.LAYOUT_OPTIONS);
            createDialog("Layout", key, adapter);
        }
    }

    private void changeLoadQuality(String key) {
        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);
            adapter.addAll(Constants.QUALITY_OPTIONS);
            createDialog("Load Quality", key, adapter);
        }
    }

    private void changeDownloadQuality(String key) {
        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);
            adapter.addAll(Constants.QUALITY_OPTIONS);
            createDialog("Download Quality", key, adapter);
        }
    }

    private void changeWallpaperQuality(String key) {
        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);
            adapter.addAll(Constants.QUALITY_OPTIONS);
            createDialog("Wallpaper Quality", key, adapter);
        }
    }

    private void handleVersion() { }

    private void handleIntro() { }

    private void handleGithub() { }

    private void reportBugs() { }

    private void showPrivacyPolicy() { }

    private void createDialog(String title, String key, final ArrayAdapter<String> adapter) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title)
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: " + adapter.getItem(which));
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: DONE");
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: CANCEL");
                        }
                    })
                    .show();
        }
    }
}