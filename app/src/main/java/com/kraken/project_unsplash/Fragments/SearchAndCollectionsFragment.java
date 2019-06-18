package com.kraken.project_unsplash.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class SearchAndCollectionsFragment extends Fragment {

    // TAG for log messages
    private static final String TAG = "SearchAndCollectionsFra";

    // root view holding the layout of the fragment
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the root view
        rootView = inflater.inflate(R.layout.fragment_collections, container, false);
        // fetch the collections
        fetchCuratedCollections();
        return rootView;
    }

    /**
     * fetch the featured collections
     */
    private void fetchCuratedCollections() {
        Log.d(TAG, "fetchPhotos: " + UrlBuilder.getFeaturedCollections(30));
        // StringRequest to fetch raw JSON
        StringRequest curatedCollectionsRequest = new StringRequest(Request.Method.GET,
                UrlBuilder.getFeaturedCollections(30), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                // serializer converts the raw JSON into Collection[]
                Serializer serializer = new Serializer();
                Collection[] collections = serializer.listCollections(response);
                // create the recycler view with rest of the Collection[]
                initRecyclerView(collections);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
            }
        }) {
            // the header parameters
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Accept-Version", "v1");
                params.put("Authorization", "Client-ID " + Constants.getAccessKey());
                return params;
            }
        };

        // add the string request to app wide local request queue
        MyApplication.getLocalRequestQueue().add(curatedCollectionsRequest);
    }

    /**
     * Init the recycler view
     * @param collections : Collection[]
     */
    private void initRecyclerView(Collection[] collections) {
        RecyclerView collectionsRecyclerView = rootView.findViewById(R.id.collectionsRecyclerView);

        CollectionsRecyclerViewAdapter adapter = new CollectionsRecyclerViewAdapter(collections, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        collectionsRecyclerView.setAdapter(adapter);
        collectionsRecyclerView.setLayoutManager(layoutManager);
    }
}
