package com.kraken.project_unsplash.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.kraken.project_unsplash.Models.AccessToken;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    final private String url = Constants.BASE_URL_AUTH + "/authorize?"
            + "client_id=" + Constants.ACCESS_KEY
            + "&response_type=code&scope=" + Constants.SCOPE
            + "&redirect_uri=" + Constants.RESPONSE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handleLoginBtn();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: got the intent");
        if (intent != null
                && intent.getData() != null
                && !intent.getData().getAuthority().isEmpty()
                && Constants.RESPONSE_URL_CALLBACK.equals(intent.getData().getAuthority())) {

            Log.d(TAG, "onNewIntent: data -> " + intent.getData());
            Log.d(TAG, "onNewIntent: authority -> " + intent.getData().getAuthority());

            getAccessToken(intent.getData().getQueryParameter("code"));
        }
    }

    private void getAccessToken(String code) {
        Log.d(TAG, "getAccessToken: code -> " + code);

        final String authUrl = Constants.BASE_URL_AUTH +
                "/token?client_id=" + Constants.ACCESS_KEY +
                "&client_secret=" + Constants.SECRET_KEY +
                "&redirect_uri=" + Constants.RESPONSE_URL +
                "&code=" + code +
                "&grant_type=authorization_code";

        final Gson gson = new Gson();
        StringRequest request = new StringRequest(Request.Method.POST, authUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 Ok\n" + response);
                AccessToken accessToken = gson.fromJson(response, AccessToken.class);
                writeAccessToken(accessToken);
                Log.d(TAG, "onResponse: access_token -> " + accessToken.getAccess_token());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ERROR");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        MyApplication.getSearchRequestQueue().add(request);
    }

    private void writeAccessToken(AccessToken accessToken) {
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.access_token_shared_preferences), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.access_token_storage_key), accessToken.getAccess_token());
        editor.apply();

        exit();
    }

    private void exit() {
        finish();
    }

    private void handleLoginBtn() {
        final Button loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(loginBtn, "Button clicked", Snackbar.LENGTH_LONG).show();

                Uri uri = Uri.parse(url);
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .setShowTitle(true)
                        .build();

                customTabsIntent.launchUrl(LoginActivity.this, uri);
            }
        });
    }
}