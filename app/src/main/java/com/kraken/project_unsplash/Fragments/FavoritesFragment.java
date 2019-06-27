package com.kraken.project_unsplash.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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

    @Override
    public void onResume() {
        super.onResume();
        // get fav photos
        getFavPhotos();
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
        PhotosRecyclerViewAdapter photosRecyclerViewAdapter = new PhotosRecyclerViewAdapter(getContext(), photos);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(photosRecyclerViewAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }
}
