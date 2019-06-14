package com.kraken.project_unsplash;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kraken.project_unsplash.Database.DatabaseContract;
import com.kraken.project_unsplash.Database.DatabaseHelper;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    /**
     * Network request queues
     * localRequestQueue - requests like photos and collections
     * searchRequestQueue - search requests
     */
    private static RequestQueue localRequestQueue, searchRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize request queues
        localRequestQueue = Volley.newRequestQueue(this);
        searchRequestQueue = Volley.newRequestQueue(this);

        Log.d(TAG, "onCreate: request queues created");
        createDatabase();
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
}
