package com.kraken.project_unsplash.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.kraken.project_unsplash.Adapters.CategoriesAdapter;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;

public class CategoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            TextView textView = findViewById(R.id.toolbar_title);
            textView.setText(getResources().getString(R.string.categories_list_activity_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        RecyclerView recyclerView = findViewById(R.id.rv_categories_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, Constants.NUM_COLUMNS);
        CategoriesAdapter adapter = new CategoriesAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
