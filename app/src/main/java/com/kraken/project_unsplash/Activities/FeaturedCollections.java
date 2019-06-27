package com.kraken.project_unsplash.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
import com.kraken.project_unsplash.Utils.Params;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.List;
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
                UrlBuilder.getFeaturedCollections(50, 1), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                // serialize into Collection[]
                Serializer serializer = new Serializer();
                List<Collection> collections = serializer.listCollections(response);
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
                return Params.getParams();
            }
        };

        // get local request queue and add the request to it
        MyApplication.getLocalRequestQueue().add(featuredCollectionsRequest);
    }

    /**
     * inflate the recycler view
     * @param collections : Collection[]
     */
    private void populateRecyclerView(List<Collection> collections) {
        RecyclerView recyclerView = findViewById(R.id.featuredCollectionsRecyclerView);
        CollectionsRecyclerViewAdapter collectionsRecyclerViewAdapter = new CollectionsRecyclerViewAdapter(this, collections);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(collectionsRecyclerViewAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }
}
