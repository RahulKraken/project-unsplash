package com.kraken.project_unsplash.Fragments;

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

    private static final String TAG = "SearchAndCollectionsFra";

    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_and_collections, container, false);
        fetchCuratedCollections();
        return rootView;
    }

    private void fetchCuratedCollections() {
        Log.d(TAG, "fetchPhotos: " + UrlBuilder.getFeaturedCollections(6));
        StringRequest curatedCollectionsRequest = new StringRequest(Request.Method.GET,
                UrlBuilder.getFeaturedCollections(7), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                Serializer serializer = new Serializer();
                Collection[] collections = serializer.listCollections(response);
                inflateMainCollection(collections[0]);
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

        MyApplication.getLocalRequestQueue().add(curatedCollectionsRequest);
    }

    private void inflateMainCollection(Collection collection) {
        ImageView mainImage = rootView.findViewById(R.id.main_collection_img);
        TextView mainImageTitle = rootView.findViewById(R.id.main_collection_title);

        Glide.with(getContext())
                .load(collection.getCover_photo().getUrls().getSmall())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background))
                .into(mainImage);
        mainImageTitle.setText(collection.getTitle());
    }

    private void initRecyclerView(Collection[] collections) {
        RecyclerView collectionsRecyclerView = rootView.findViewById(R.id.collectionsRecyclerView);

        CollectionsRecyclerViewAdapter adapter = new CollectionsRecyclerViewAdapter(collections, getContext());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        collectionsRecyclerView.setAdapter(adapter);
        collectionsRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }
}
