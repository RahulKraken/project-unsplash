package com.kraken.project_unsplash.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kraken.project_unsplash.Adapters.CategoriesAdapter;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        RecyclerView recyclerView = findViewById(R.id.rv_categories_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, Constants.NUM_COLUMNS);
        CategoriesAdapter adapter = new CategoriesAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
