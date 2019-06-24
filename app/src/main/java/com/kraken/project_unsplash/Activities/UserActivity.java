package com.kraken.project_unsplash.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kraken.project_unsplash.Fragments.CollectionsFragment;
import com.kraken.project_unsplash.Fragments.FeaturedPhotosFragment;
import com.kraken.project_unsplash.Fragments.NewPhotosFragment;
import com.kraken.project_unsplash.R;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // handle the collapsing toolbar
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.user_page_collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.user_page_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("@kraken");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viewPager = findViewById(R.id.user_page_view_pager);
        tabLayout = findViewById(R.id.user_page_tab_layout);

        setupViewPager();
        setupTabLayout();
    }
    
    @SuppressWarnings("ConstantConditions")
    private void setupTabLayout() {
        Log.d(TAG, "setupTabLayout: Setting up tabLayout");
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("NEW");
        tabLayout.getTabAt(1).setText("LIKED");
        tabLayout.getTabAt(2).setText("COLLECTIONS");

        tabLayout.setSelected(true);
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new NewPhotosFragment(), "Photos");
        viewPagerAdapter.addFragment(new FeaturedPhotosFragment(), "Featured");
        viewPagerAdapter.addFragment(new CollectionsFragment(), "Collections");

        viewPager.setAdapter(viewPagerAdapter);
    }

    // -------------------- Pager Adapter --------------------

    class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }
}