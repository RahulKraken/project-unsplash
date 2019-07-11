package com.kraken.project_unsplash.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.kraken.project_unsplash.Utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    // widgets
    private RecyclerView recyclerView;
    private EditText etSearchKey;

    // content
    private ArrayList<Photo> photos;
    private PhotosRecyclerViewAdapter adapter;
    private int currentItemCnt, pastItemCnt, totalItemCnt;


    // page to fetch
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.rv_search);
        etSearchKey = findViewById(R.id.et_search_key);
        ImageButton searchBtn = findViewById(R.id.search_btn);

        photos = new ArrayList<>();
        adapter = new PhotosRecyclerViewAdapter(this, photos);

        // action listener on edit text
        etSearchKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getPhotos(StringUtils.getKeyword(String.valueOf(etSearchKey.getText())));
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

         // listen to clicks on button and fetch search results
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchResults();
            }
        });
        initRecyclerView();
    }

    /**
     * hide soft keyboard when search pressed
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * gets key from edit text and formats the string carefully
     * and triggers the api request
     */
    private void fetchResults() {
        String key = String.valueOf(etSearchKey.getText());
        if (key.trim().length() > 0) {
            key = StringUtils.getKeyword(key);
            getPhotos(key);
        }
        else Toast.makeText(this, "Enter search key", Toast.LENGTH_SHORT).show();
    }

    /**
     * fetch result from end point
     * @param key : keyword to search for
     */
    private void getPhotos(String key) {
        Log.d(TAG, "getPhotos: " + UrlBuilder.searchPhoto(key, page));
        StringRequest searchImageRequest = new StringRequest(Request.Method.GET, UrlBuilder.searchPhoto(key, page), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                try {
                    // convert response into JSON object and get the "results" array
                    JSONObject jsonObject = new JSONObject(response);
                    String raw = jsonObject.getString("results");
                    Log.d(TAG, "onResponse: RAW " + raw);

                    // serialize the extracted results array of photos and populate the arrayList
                    photos.addAll(new Serializer().listPhotos(raw));
                    // notify adapter to show new content
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
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
                return Params.getParams(SearchActivity.this);
            }
        };

        page++;

        MyApplication.getSearchRequestQueue().add(searchImageRequest);
    }

    /**
     * initialize recycler view and onScrollListener
     */
    private void initRecyclerView() {
        final GridLayoutManager layoutManager = new GridLayoutManager(this, Constants.NUM_COLUMNS);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        // set onScrollListener to find the end of scroll and fetch more results
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                totalItemCnt = layoutManager.getItemCount();
                currentItemCnt = recyclerView.getChildCount();
                pastItemCnt = layoutManager.findFirstVisibleItemPosition();

                if (currentItemCnt + pastItemCnt >= totalItemCnt) {
                    Toast.makeText(SearchActivity.this, "Reached end", Toast.LENGTH_SHORT).show();
                    fetchResults();
                }
            }
        });
    }
}
