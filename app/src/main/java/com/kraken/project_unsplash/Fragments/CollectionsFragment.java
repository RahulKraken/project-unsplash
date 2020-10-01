package com.kraken.project_unsplash.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.kraken.project_unsplash.Adapters.CollectionsRecyclerViewAdapter;
import com.kraken.project_unsplash.Models.Collection;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;
import com.kraken.project_unsplash.Utils.Params;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectionsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

  private static final String TAG = "CollectionsFragment";

  // root view holding the layout of the fragment
  private View rootView;
  private int page = 1;

  // data
  private List<Collection> collections;

  // recycler view scroll stuff
  private int pastVisibleItems, visibleItemCount, totalItemCount;
  private CollectionsRecyclerViewAdapter recyclerViewAdapter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    MyApplication.preferences.registerOnSharedPreferenceChangeListener(this);

    // init data
    collections = new ArrayList<>();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // inflate the root view
    rootView = inflater.inflate(R.layout.fragment_collections, container, false);
    // fetch the collections
    fetchCuratedCollections();
    // init the recycler view
    initRecyclerView();
    return rootView;
  }

  /**
   * fetch the featured collections
   */
  private void fetchCuratedCollections() {
    Log.d(TAG, "fetchPhotos: " + UrlBuilder.getFeaturedCollections(30, page));
    // StringRequest to fetch raw JSON
    StringRequest curatedCollectionsRequest = new StringRequest(Request.Method.GET,
      UrlBuilder.getFeaturedCollections(30, page),
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          Log.d(TAG, "onResponse: 200 OK\n" + response);
          // serializer converts the raw JSON into Collection[]
          Serializer serializer = new Serializer();
          collections.addAll(serializer.listCollections(response));
          // notify adapter of the data change
          recyclerViewAdapter.notifyDataSetChanged();
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
        return Params.getParams(getActivity());
      }
    };

    // increment page
    page += 1;

    // add the string request to app wide local request queue
    MyApplication.getLocalRequestQueue().add(curatedCollectionsRequest);
  }

  /**
   * Init the recycler view
   */
  private void initRecyclerView() {
    RecyclerView collectionsRecyclerView = rootView.findViewById(R.id.collectionsRecyclerView);

    recyclerViewAdapter = new CollectionsRecyclerViewAdapter(getContext(), collections);
    final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

    collectionsRecyclerView.setAdapter(recyclerViewAdapter);
    collectionsRecyclerView.setLayoutManager(layoutManager);

    // add onScrollListener on recycler view to enable continuous indefinite scroll
    collectionsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        // if scroll up
        if (dy > 0) {
          // get counts of past items, currently visible items and total items
          visibleItemCount = recyclerView.getChildCount();
          totalItemCount = layoutManager.getItemCount();
          pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

          // if visible cnt + past item cnt >= total items cnt then we've reached
          // the end
          // we can also change the total item count to be some integer less than
          // that to allow for continuous scroll without breaks
          if (visibleItemCount + pastVisibleItems >= totalItemCount) {
            Toast.makeText(getContext(), "reached end", Toast.LENGTH_SHORT).show();
            // when end reached fetch more content
            fetchCuratedCollections();
          }
        }
      }
    });
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals("pref_theme")) {
      Log.d(TAG, "onSharedPreferenceChanged: theme changes");
    }
  }
}
