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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kraken.project_unsplash.Adapters.PhotosRecyclerViewAdapter;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment class for the Featured Photo section
 * selected by default
 */

public class PhotosFragment extends Fragment {

    private static final String TAG = "PhotosFragment";

    // root view of the fragment
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the view and keep in rootView
        rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        // run the network task to fetch photos before returning the view
        fetchPhotos();
        return rootView;
    }

    /**
     * Use the localRequestQueue to fetch featured photos
     */
    private void fetchPhotos() {
        Log.d(TAG, "fetchPhotos: " + UrlBuilder.getAllPhotos(50));
        // string request fetches raw json using volley
        StringRequest allPhotosRequest = new StringRequest(Request.Method.GET, UrlBuilder.getAllPhotos(50),
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                // serializer converts the raw json into a Photo[]
                Serializer serializer = new Serializer();
                List<Photo> photos = serializer.listPhotos(response);
                // create the recycler view with the photos
                initRecyclerView(photos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                // make a toast with the error
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

        // add the string request to the local request queue in the application context;
        MyApplication.getLocalRequestQueue().add(allPhotosRequest);
    }

    /**
     * create the recycler view
     * @param photos : Photo[]
     */
    private void initRecyclerView(List<Photo> photos) {
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_featured_photos);

        PhotosRecyclerViewAdapter photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(getContext(), photos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setAdapter(photosRecyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
