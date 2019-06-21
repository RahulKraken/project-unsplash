package com.kraken.project_unsplash.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.R;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // redirect_uri = com.kraken.project_unsplash:/oauth2redirect
    private String url = "https://unsplash.com/oauth/authorize?client_id=15dbbf8531d6a54d5325124fe9a0f21b897aa7b27b09c982d01cf3b5dd92256c&response_type=code&scope=public+read_user&redirect_uri=kraken://unsplash-auth-callback";

    private String callback = "unsplash-auth-callback";

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
                && callback.equals(intent.getData().getAuthority())) {
            Log.d(TAG, "onNewIntent: data -> " + intent.getData());
            Log.d(TAG, "onNewIntent: authority -> " + intent.getData().getAuthority());
            getAccessToken(intent.getData().getQueryParameter("code"));
        }
    }

    private void getAccessToken(String code) {
        Log.d(TAG, "getAccessToken: code -> " + code);

        String authUrl = "https://unsplash.com/oauth/token?client_id=15dbbf8531d6a54d5325124fe9a0f21b897aa7b27b09c982d01cf3b5dd92256c&client_secret=0a20b1c09a09db21767dc2fc691dd2d3083c2b7b279eb460c78bc00e075b3309&redirect_uri=kraken://unsplash-auth-callback&code=" + code + "&grant_type=authorization_code";

        final Gson gson = new Gson();
        StringRequest request = new StringRequest(Request.Method.POST, authUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 Ok\n" + response);
                AccessToken accessToken = gson.fromJson(response, AccessToken.class);
                Log.d(TAG, "onResponse: access_token -> " + accessToken.access_token);
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
/*

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
*/
            }
        });
    }
}

class AccessToken {
    String access_token, token_type, refresh_token, scope;
    int created_at;
}