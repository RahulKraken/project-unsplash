package com.kraken.project_unsplash.Activities;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.kraken.project_unsplash.Fragments.CollectionsFragment;
import com.kraken.project_unsplash.Fragments.FeaturedPhotosFragment;
import com.kraken.project_unsplash.Fragments.NewPhotosFragment;
import com.kraken.project_unsplash.Models.User;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Params;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView photoCnt, collectionCnt, name, bio;
    private ImageView profilePic;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // get intent extra
        Intent intent = getIntent();
        userName = intent.getStringExtra(getResources().getString(R.string.user_itent_pass_key));

        // handle the collapsing toolbar
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.user_page_collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.user_page_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // get widgets
        photoCnt = findViewById(R.id.user_page_photo_cnt);
        collectionCnt = findViewById(R.id.user_page_collection_cnt);
        name = findViewById(R.id.user_page_full_name);
        bio = findViewById(R.id.user_page_bio);
        profilePic = findViewById(R.id.user_page_profile_pic);

        getUserDetails();

        viewPager = findViewById(R.id.user_page_view_pager);
        tabLayout = findViewById(R.id.user_page_tab_layout);

        setupViewPager();
        setupTabLayout();
    }

    private void getUserDetails() {
        StringRequest getUserRequest = new StringRequest(Request.Method.GET, UrlBuilder.getUser(userName), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                User user = new Serializer().getUser(response);
                setProfilePic(user);
                populateTextData(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Params.getParams();
            }
        };

        Log.d(TAG, "getUserDetails: " + getUserRequest.toString());
        MyApplication.getSearchRequestQueue().add(getUserRequest);
    }

    private void setProfilePic(User user) {
        Glide.with(this)
                .load(user.getProfile_image().getLarge())
                .into(profilePic);
    }

    private void populateTextData(User user) {
        photoCnt.setText(String.valueOf(user.getTotal_photos()));
        collectionCnt.setText(String.valueOf(user.getTotal_collections()));
        name.setText(user.getName());
        bio.setText(user.getBio());
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(user.getUsername());
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