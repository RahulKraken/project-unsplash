package com.kraken.project_unsplash.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
    createDialog("Theme", key, Constants.THEME_OPTIONS);
  }

  private void changeLayoutValue(String key) {
    createDialog("Layout", key, Constants.LAYOUT_OPTIONS);
  }

  private void changeLoadQuality(String key) {
    createDialog("Load Quality", key, Constants.QUALITY_OPTIONS);
  }

  private void changeDownloadQuality(String key) {
    createDialog("Download Quality", key, Constants.QUALITY_OPTIONS);
  }

  private void changeWallpaperQuality(String key) {
    createDialog("Wallpaper Quality", key, Constants.QUALITY_OPTIONS);
  }

  private void handleVersion() {
    // todo : change version dynamically
    Toast.makeText(getActivity(), "1.2.3", Toast.LENGTH_SHORT).show();
  }

  private void handleGithub() {
    Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RahulKraken/project-unsplash"));
    startActivity(githubIntent);
  }

  private void reportBugs() {
    // todo : put a better bug reporting system
    Intent issueIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RahulKraken/project-unsplash/issues"));
    startActivity(issueIntent);
  }

  private void showPrivacyPolicy() {
    // todo : put privacy policy in place and display
    Toast.makeText(getActivity(), "Privacy Policy under construction", Toast.LENGTH_SHORT).show();
  }

  private void createDialog(String title, final String key, List<String> list) {
    if (getActivity() != null) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
      adapter.addAll(list);
      builder.setTitle(title)
        .setAdapter(adapter, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, adapter.getItem(which));
            editor.apply();
            Log.d(TAG, "onClick: " + sharedPreferences.getString(key, null));
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