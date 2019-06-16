package com.kraken.project_unsplash.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kraken.project_unsplash.Adapters.PhotosRecyclerViewAdapter;
import com.kraken.project_unsplash.Database.DatabaseContract;
import com.kraken.project_unsplash.Database.DatabaseHelper;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";

    // root view holding the entire fragment
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        getFavPhotos();
        return rootView;
    }

    /**
     * get List of Photos from database
     */
    private void getFavPhotos() {
        // get the database
        DatabaseHelper helper = new DatabaseHelper(getContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        // query database
        String[] columns = {DatabaseContract.FavoritesEntry.COLUMN_PHOTO};
        Cursor cursor = database.query(DatabaseContract.FavoritesEntry.TABLE_NAME, columns, null,
                null, null, null, null);

        // get list of photos
        List<Photo> photos = new ArrayList<>();
        while (cursor.moveToNext()) {
            photos.add(
                    (Photo) Serializer.photoFromByteArray(cursor.getBlob(cursor.getColumnIndex(DatabaseContract.FavoritesEntry.COLUMN_PHOTO))));
        }

        cursor.close();
        initRecyclerView(photos);
    }

    /**
     * inflate the recycler view with photos
     * @param photos : List<Photo>
     */
    private void initRecyclerView(List<Photo> photos) {
        RecyclerView recyclerView = rootView.findViewById(R.id.fav_recycler_view);
        PhotosRecyclerViewAdapter photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(getContext(), photos.toArray(new Photo[0]));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(photosRecyclerViewAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }
}
