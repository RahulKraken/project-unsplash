package com.kraken.project_unsplash.Utils;

import android.util.Log;

import com.google.gson.Gson;
import com.kraken.project_unsplash.Models.Collection;
import com.kraken.project_unsplash.Models.Photo;

public class Serializer {

    private static final String TAG = "Serializer";

    private Gson gson;

    public Serializer() {
        gson = new Gson();
    }

    public Photo[] listPhotos(String raw) {
        Photo[] photos = gson.fromJson(raw, Photo[].class);
        Log.d(TAG, "listPhotos: " + photos.length);
        return photos;
    }

    public Collection[] listCollections(String raw) {
        Collection[] collections = gson.fromJson(raw, Collection[].class);
        Log.d(TAG, "listCollections: " + collections.length);
        return collections;
    }
}
