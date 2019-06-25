package com.kraken.project_unsplash.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kraken.project_unsplash.Fragments.CollectionsFragment;
import com.kraken.project_unsplash.Fragments.FeaturedPhotosFragment;
import com.kraken.project_unsplash.Fragments.NewPhotosFragment;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    // widgets
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private TextView toolbarTitle;

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
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // setting up bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.frame_layout_main_activity);
        setupBottomNavigation();
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
            return true;
        }

        // if sort by button is selected
        if (id == R.id.action_sort) {
            buildDialogBox();
            return true;
        }

        if (id == R.id.action_login) {
            startActivity(new Intent(this, LoginActivity.class));
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
                startActivity(new Intent(this, CategoriesActivity.class));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
