package com.kraken.project_unsplash;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kraken.project_unsplash.Activities.MainActivity;
import com.kraken.project_unsplash.Database.DatabaseContract;
import com.kraken.project_unsplash.Database.DatabaseHelper;
import com.kraken.project_unsplash.Models.User;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.Utils.Params;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

  private static final String TAG = "MyApplication";

  public static boolean AUTHENTICATED = false;

  public static User me;

  private static Context context;

  private static FirebaseAnalytics firebaseAnalytics;

  /**
   * Network request queues
   * localRequestQueue - requests like photos and collections
   * searchRequestQueue - search requests
   */
  private static RequestQueue localRequestQueue, searchRequestQueue;

  public static SharedPreferences preferences;

  @Override
  public void onCreate() {
    super.onCreate();

    context = getApplicationContext();

    // initialize firebase analytics
    firebaseAnalytics = FirebaseAnalytics.getInstance(this);

    // load default preferences
    PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false);
    preferences = PreferenceManager.getDefaultSharedPreferences(this);

    // initialize request queues
    localRequestQueue = Volley.newRequestQueue(this);
    searchRequestQueue = Volley.newRequestQueue(this);

    Log.d(TAG, "onCreate: request queues created");

//    checkAuthenticationState();

    createDatabase();
  }

  public static void logEvent(String id, String name, String contentType) {
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
    MyApplication.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
  }

  public void checkAuthenticationState() {
    // check if authenticated
    StringRequest checkIfAuthenticatedRequest = new StringRequest(Request.Method.GET, UrlBuilder.getProfile(), new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        Log.d(TAG, "onResponse: 200 OK\n" + response);
        me = new Serializer().getUser(response);
        MainActivity.populateNavHeader();
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse: " + error.toString());
      }
    }) {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return Params.getAuthenticatedParams(MyApplication.this);
      }

      @Override
      protected Response<String> parseNetworkResponse(NetworkResponse response) {
        int responseCode = response.statusCode;
        if (responseCode == 200) {
          MyApplication.AUTHENTICATED = true;
          Log.d(TAG, "parseNetworkResponse: AUTHENTICATED");
        } else {
          MyApplication.AUTHENTICATED = false;
          Log.d(TAG, "parseNetworkResponse: NOT AUTHENTICATED");
          SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.access_token_shared_preferences), MODE_PRIVATE).edit();
          editor.putString(getResources().getString(R.string.access_token_storage_key), null);
          editor.apply();
        }
        return super.parseNetworkResponse(response);
      }
    };

    localRequestQueue.add(checkIfAuthenticatedRequest);
  }

  /**
   * testing interaction with the database
   */
  private void createDatabase() {
    new DatabaseHelper(this);
  }

  /**
   * getters for request queues
   * @return requestQueue
   */
  public static RequestQueue getLocalRequestQueue() {
    return localRequestQueue;
  }

  public static RequestQueue getSearchRequestQueue() {
    return searchRequestQueue;
  }

  public static Context getAppContext() {
    return context;
  }
}
