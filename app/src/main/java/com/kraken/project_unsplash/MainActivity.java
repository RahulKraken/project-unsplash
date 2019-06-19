package com.kraken.project_unsplash;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.kraken.project_unsplash.Fragments.FeaturedPhotosFragment;
import com.kraken.project_unsplash.Fragments.NewPhotosFragment;
import com.kraken.project_unsplash.Fragments.CollectionsFragment;

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

    /**
     * setup bottom nav bar
     * @param fragment : default fragment
     */
    private void setUpBottomNavigation(@Nullable Fragment fragment) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (fragment == null) fragment = new NewPhotosFragment();
        setFragment(fragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_item_featured:
                        setFragment(new NewPhotosFragment());
                        return true;
                    case R.id.nav_item_search:
                        setFragment(new FeaturedPhotosFragment());
                        return true;
                    case R.id.nav_item_favorites:
                        setFragment(new CollectionsFragment());
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * set @param "fragment" into the frame layout
     * @param fragment : fragment
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.featured_photos_fragment_frame, fragment);
        fragmentTransaction.commit();
    }
}