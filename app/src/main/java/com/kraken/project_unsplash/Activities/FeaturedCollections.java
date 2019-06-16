package com.kraken.project_unsplash.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kraken.project_unsplash.Adapters.CollectionsRecyclerViewAdapter;
import com.kraken.project_unsplash.Models.Collection;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.HashMap;
import java.util.Map;

public class FeaturedCollections extends AppCompatActivity {

    private static final String TAG = "FeaturedCollections";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_collections);

        // get collections
        getCollections();
    }

    /**
     * get featured collections
     */
    private void getCollections() {
        StringRequest featuredCollectionsRequest = new StringRequest(Request.Method.GET,
                UrlBuilder.getFeaturedCollections(50), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                // serialize into Collection[]
                Serializer serializer = new Serializer();
                Collection[] collections = serializer.listCollections(response);
                // inflate the recycler view with Collection[]
                populateRecyclerView(collections);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Toast.makeText(FeaturedCollections.this, "Failed to get Collections!!", Toast.LENGTH_SHORT).show();
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

        // get local request queue and add the request to it
        MyApplication.getLocalRequestQueue().add(featuredCollectionsRequest);
    }

    /**
     * inflate the recycler view
     * @param collections : Collection[]
     */
    private void populateRecyclerView(Collection[] collections) {
        RecyclerView recyclerView = findViewById(R.id.featuredCollectionsRecyclerView);
        CollectionsRecyclerViewAdapter collectionsRecyclerViewAdapter = new CollectionsRecyclerViewAdapter(collections, this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(collectionsRecyclerViewAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }
}
