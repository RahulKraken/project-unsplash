package com.kraken.project_unsplash.Activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kraken.project_unsplash.Fragments.CollectionsFragment;
import com.kraken.project_unsplash.Fragments.FeaturedPhotosFragment;
import com.kraken.project_unsplash.Fragments.NewPhotosFragment;
import com.kraken.project_unsplash.Models.User;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    // widgets
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private TextView toolbarTitle;
    public static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        // setting up the drawer layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        handleNavigationViewHeader();

        // setting up bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.frame_layout_main_activity);
        setupBottomNavigation();
    }

    private void handleNavigationViewHeader() {
        final View header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.AUTHENTICATED) {
                    launchProfile("me");
                } else {
                    launchProfile("login");
                }
            }

            private void launchProfile(String key) {
                if (key.equals("me")) {
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    intent.putExtra(getResources().getString(R.string.profile_intent_pass_key), "me");
                    startActivity(intent);
                } else if (key.equals("login")){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
    }

    public static void populateNavHeader() {
        View header = navigationView.getHeaderView(0);

        ImageView imageView = header.findViewById(R.id.nav_header_profile_image);
        TextView name = header.findViewById(R.id.nav_header_name);
        TextView username = header.findViewById(R.id.nav_header_username);

        Log.d(TAG, "populateNavHeader: " + MyApplication.me.getUsername());
        Log.d(TAG, "populateNavHeader: " + MyApplication.me.getName());

        name.setText(MyApplication.me.getName());
        username.setText(MyApplication.me.getUsername());
        Glide.with(MyApplication.getAppContext())
                .load(MyApplication.me.getProfile_image().getSmall())
                .apply(new RequestOptions().placeholder(R.color.md_grey_300))
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Log.d(TAG, "onOptionsItemSelected: launching search activity");
            startActivity(new Intent(this, SearchActivity.class));
        }

        // if sort by button is selected
        if (id == R.id.action_sort) {
            buildDialogBox();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_new:
                Log.d(TAG, "onNavigationItemSelected: navigating to new photos");
                bottomNavigationView.setSelectedItemId(R.id.nav_item_new);
                break;
            case R.id.nav_featured:
                Log.d(TAG, "onNavigationItemSelected: navigating to featured");
                bottomNavigationView.setSelectedItemId(R.id.nav_item_featured);
                break;
            case R.id.nav_collections:
                Log.d(TAG, "onNavigationItemSelected: navigating to collections");
                bottomNavigationView.setSelectedItemId(R.id.nav_item_collections);
                break;
            case R.id.nav_categories:
                startActivity(new Intent(this, CategoryListActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, PreferenceActivity.class));
                break;
            case R.id.nav_rate:
                openGooglePlayStore();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * open google play store app page to rate
     */
    private void openGooglePlayStore() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Log.d(TAG, "openGooglePlayStore: " + uri.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    /**
     * set up bottom navigation view with navigationItemSelectedListener
     */
    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_item_new:
                        Log.d(TAG, "onNavigationItemSelected: new photos fragment");
                        setFragment(new NewPhotosFragment());
                        setToolbarTitle(getResources().getString(R.string.nav_item_new_label));
                        return true;
                    case R.id.nav_item_featured:
                        Log.d(TAG, "onNavigationItemSelected: featured photos fragment");
                        setFragment(new FeaturedPhotosFragment());
                        setToolbarTitle(getResources().getString(R.string.featured_nav_item_label));
                        return true;
                    case R.id.nav_item_collections:
                        Log.d(TAG, "onNavigationItemSelected: collections fragment");
                        setFragment(new CollectionsFragment());
                        setToolbarTitle(getResources().getString(R.string.collections_nav_bar_label));
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_item_new);
    }

    private void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    /**
     * put fragment in place of frame layout
     * @param fragment : Fragment
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    /**
     * build sort by options dialog box
     */
    private void buildDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
        adapter.addAll(Constants.ORDER_BY_OPTIONS);

        builder.setNegativeButton(getResources().getString(R.string.dialog_cancel_btn_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendToFragment(adapter.getItem(which));
            }
        });

        builder.show();
    }

    /**
     * sort by dialog function to update the recycler view
     * @param param : parameter to sort the results
     */
    private void sendToFragment(String param) {
        Log.d(TAG, "sendToFragment: " + param);

        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.order_by_param_transaction_key), param);

        int currentFragmentIdx = bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId()).getItemId();

        switch (currentFragmentIdx) {
            case R.id.nav_item_new:
                Log.d(TAG, "sendToFragment: NewPhotosFragment");
                Fragment newPhotosFragment = new NewPhotosFragment();
                newPhotosFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main_activity, newPhotosFragment).commit();
                break;
            case R.id.nav_item_featured:
                Log.d(TAG, "sendToFragment: FeaturedPhotosFragment");
                Fragment featuredPhotosFragment = new FeaturedPhotosFragment();
                featuredPhotosFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main_activity, featuredPhotosFragment).commit();
                break;
        }
    }
}
