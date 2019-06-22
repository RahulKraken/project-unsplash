package com.kraken.project_unsplash.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kraken.project_unsplash.Adapters.PhotosRecyclerViewAdapter;
import com.kraken.project_unsplash.Models.Collection;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;
import com.kraken.project_unsplash.Utils.Params;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionView extends AppCompatActivity {

    private static final String TAG = "CollectionView";

    // current collection
    private Collection collection;

    // recycler view scroll stuff
    private PhotosRecyclerViewAdapter recyclerViewAdapter;
    private int pastItemsCount, visibleItemCount, totalItemCount;
    private int page = 1;

    private List<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_view);

        // setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = findViewById(R.id.toolbar_title);

        photos = new ArrayList<>();

        // get collection from the intent
        collection = (Collection) getIntent().getSerializableExtra(getResources().getString(R.string.collection_parcelable_intent_extra));

        Log.d(TAG, "onCreate: " + collection.getId());

        // set title of the collection
        String title = collection.getTitle() != null ? collection.getTitle() : getResources().getString(R.string.collection_title_replacement);
        if (getSupportActionBar() != null) {
            toolbar_title.setText(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Log.d(TAG, "onCreate: description " + collection.getDescription());

        // get photos belonging to collection
        initRecyclerView();
        getPhotosForCollection();
    }

    /**
     * get photos belonging to the current collection
     */
    private void getPhotosForCollection() {
        // string request to get raw json for the photos
        StringRequest photosForCollectionRequest =
                new StringRequest(Request.Method.GET, UrlBuilder.getCollectionPhotos(collection.getId(), null, page), new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: 200 OK\n" + response);
                        // serialize raw json into Photo[]
                        Serializer serializer = new Serializer();
                        photos.addAll(serializer.listPhotos(response));
                        // notify data set changed
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
                        Toast.makeText(CollectionView.this, "Cannot fetch photos", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return Params.getParams();
                    }
                };

        page += 1;

        // get local request queue and add request to it
        MyApplication.getLocalRequestQueue().add(photosForCollectionRequest);
    }

    /**
     * inflate the recycler view
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.collectionViewRecyclerView);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, Constants.NUM_COLUMNS);
        recyclerViewAdapter = new PhotosRecyclerViewAdapter(this, photos);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // if scrolled down
                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastItemsCount = layoutManager.findFirstVisibleItemPosition();

                    // if end reached
                    if (visibleItemCount + pastItemsCount >= totalItemCount) {
                        getPhotosForCollection();
                    }
                }
            }
        });
    }
}
