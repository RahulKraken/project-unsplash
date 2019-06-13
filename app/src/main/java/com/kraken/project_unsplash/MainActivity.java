package com.kraken.project_unsplash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kraken.project_unsplash.Adapters.StaggeredRecyclerViewAdapter;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.Utils.Constants;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * MainActivity class
 * The Application starts from here
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchPhotos();
    }

    private void fetchPhotos() {
        Log.d(TAG, "fetchPhotos: " + UrlBuilder.getAllPhotos(20));
        StringRequest allPhotosRequest = new StringRequest(Request.Method.GET,
                UrlBuilder.getAllPhotos(20), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                Serializer serializer = new Serializer();
                Photo[] photos = serializer.listPhotos(response);
                initRecyclerView(photos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                Toast.makeText(MainActivity.this, error.getCause().toString(), Toast.LENGTH_LONG).show();
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

        MyApplication.getLocalRequestQueue().add(allPhotosRequest);
    }

    private void initRecyclerView(Photo[] photos) {
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);

        StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter(this, photos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}