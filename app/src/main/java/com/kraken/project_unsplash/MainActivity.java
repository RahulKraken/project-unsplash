package com.kraken.project_unsplash;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.kraken.project_unsplash.Fragments.FavoritesFragment;
import com.kraken.project_unsplash.Fragments.FeaturedPhotosFragment;
import com.kraken.project_unsplash.Fragments.SearchAndCollectionsFragment;

/**
 * MainActivity class
 * The Application starts from here
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpBottomNavigation(null);
    }

    private void setUpBottomNavigation(@Nullable Fragment fragment) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (fragment == null) fragment = new FeaturedPhotosFragment();
        setFragment(fragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_item_featured:
                        setFragment(new FeaturedPhotosFragment());
                        return true;
                    case R.id.nav_item_search:
                        setFragment(new SearchAndCollectionsFragment());
                        return true;
                    case R.id.nav_item_favorites:
                        setFragment(new FavoritesFragment());
                        return true;
                    case R.id.nav_item_profile:
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.featured_photos_fragment_frame, fragment);
        fragmentTransaction.commit();
    }
}