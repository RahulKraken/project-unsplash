package com.kraken.project_unsplash.Network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.Utils.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    private Context context;

    public NetworkUtils(Context context) {
        this.context = context;
    }

    public void getJSON(final String url) {
        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "onResponse: " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "onErrorResponse: " + error.toString());
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Accept-Version", "v1");
                        params.put("Authorization", "Client-ID " + Constants.getAccessKey());
                        return params;
                    }
                };

        MyApplication.getLocalRequestQueue().add(stringRequest);
    }
}
