package com.kraken.project_unsplash.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
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
import com.kraken.project_unsplash.Utils.Params;
import com.kraken.project_unsplash.Utils.Serializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";

    private PhotosRecyclerViewAdapter adapter;

    private List<Photo> photos;
    private int page = 1;

    int visibleItemCount, totalItemCount, pastItemCount;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        category = getIntent().getStringExtra(getResources().getString(R.string.category_intent_pass_key));
        Toast.makeText(this, category, Toast.LENGTH_LONG).show();

        if (getSupportActionBar() != null) {
            TextView textView = findViewById(R.id.toolbar_title);
            textView.setText(category);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        photos = new ArrayList<>();
        adapter = new PhotosRecyclerViewAdapter(this, photos);
        initRecyclerView();
        fetchPhotos();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_category_activity);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, Constants.NUM_COLUMNS);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();

                    if (visibleItemCount + pastItemCount >= totalItemCount) {
                        fetchPhotos();
                        Toast.makeText(CategoryActivity.this, "reached end", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void fetchPhotos() {
        Log.d(TAG, "fetchPhotos: " + UrlBuilder.getCategoryUrl(category, page));
        StringRequest photosRequest = new StringRequest(Request.Method.GET, UrlBuilder.getCategoryUrl(category, page), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String res = jsonObject.getString("results");
                    photos.addAll(new Serializer().listPhotos(res));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Params.getParams();
            }
        };

        page++;

        MyApplication.getLocalRequestQueue().add(photosRequest);
    }
}
