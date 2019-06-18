package com.kraken.project_unsplash.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kraken.project_unsplash.Adapters.CollectionsRecyclerViewAdapter;
import com.kraken.project_unsplash.Activities.FeaturedCollections;
import com.kraken.project_unsplash.Models.Collection;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.Arrays;
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
        addOnClickListener();
        return rootView;
    }

    /**
     * add onClickListener to the view more button
     */
    private void addOnClickListener() {
        Button viewMoreBtn = rootView.findViewById(R.id.viewMoreBtn);
        viewMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FeaturedCollections.class));
            }
        });
    }

    /**
     * fetch the featured collections
     */
    private void fetchCuratedCollections() {
        Log.d(TAG, "fetchPhotos: " + UrlBuilder.getFeaturedCollections(6));
        // StringRequest to fetch raw JSON
        StringRequest curatedCollectionsRequest = new StringRequest(Request.Method.GET,
                UrlBuilder.getFeaturedCollections(7), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                // serializer converts the raw JSON into Collection[]
                Serializer serializer = new Serializer();
                Collection[] collections = serializer.listCollections(response);
                // inflate the main Collection card
                inflateMainCollection(collections[0]);
                // create the recycler view with rest of the Collection[]
                initRecyclerView(Arrays.copyOfRange(collections, 1, 7));
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
     * populate the main collection card
     * @param collection : Collection obj
     */
    private void inflateMainCollection(Collection collection) {
        // image view and text view of the main collection card
        ImageView mainImage = rootView.findViewById(R.id.main_collection_img);
        TextView mainImageTitle = rootView.findViewById(R.id.main_collection_title);

        // using glide to populate the main image view
        Glide.with(getContext())
                .load(collection.getCover_photo().getUrls().getSmall())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background))
                .into(mainImage);
        // set the title of the card
        mainImageTitle.setText(collection.getTitle());
    }

    /**
     * Init the recycler view
     * @param collections : Collection[]
     */
    private void initRecyclerView(Collection[] collections) {
        RecyclerView collectionsRecyclerView = rootView.findViewById(R.id.collectionsRecyclerView);

        CollectionsRecyclerViewAdapter adapter = new CollectionsRecyclerViewAdapter(collections, getContext());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        collectionsRecyclerView.setAdapter(adapter);
        collectionsRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }
}
