package com.kraken.project_unsplash.Activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.HashMap;
import java.util.Map;

public class CollectionView extends AppCompatActivity {

    private static final String TAG = "CollectionView";

    private Collection collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_view);

        TextView tv_title = findViewById(R.id.collectionTitle);

        collection = (Collection) getIntent().getSerializableExtra(getResources().getString(R.string.collection_parcelable_intent_extra));

        Log.d(TAG, "onCreate: " + collection.getId());

        String title = collection.getTitle() != null ? collection.getTitle() : getResources().getString(R.string.collection_title_replacement);
        tv_title.setText(title);

        Log.d(TAG, "onCreate: description " + collection.getDescription());

        if (collection.getDescription() != null) {
            TextView tv_desc = new TextView(this, null, 0, R.style.DescStyle);
            tv_desc.setTextSize(14);
            tv_desc.setPadding(14, 14, 14, 0);
            tv_desc.setTextColor(ContextCompat.getColor(this, R.color.md_black_1000));
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(14, 14, 14, 0);
            tv_desc.setLayoutParams(params);
            tv_desc.setText(collection.getDescription());

            LinearLayout linearLayout = findViewById(R.id.collectionViewLinearLayout);
            linearLayout.addView(tv_desc, 1);

            NestedScrollView nestedScrollView = findViewById(R.id.collectionViewNestedScrollView);
            nestedScrollView.invalidate();
            nestedScrollView.requestLayout();
        }

        getPhotosForCollection();
    }

    private void getPhotosForCollection() {
        StringRequest photosForCollectionRequest =
                new StringRequest(Request.Method.GET, UrlBuilder.getCollectionPhotos(collection.getId()), new Response.Listener<String>() {
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
                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
                        Toast.makeText(CollectionView.this, "Cannot fetch photos", Toast.LENGTH_SHORT).show();
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

        MyApplication.getLocalRequestQueue().add(photosForCollectionRequest);
    }

    private void initRecyclerView(Photo[] photos) {
        RecyclerView recyclerView = findViewById(R.id.collectionViewRecyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        PhotosRecyclerViewAdapter photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(this, photos);

        recyclerView.setAdapter(photosRecyclerViewAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }
}
