package com.ohadshai.movielib.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.ohadshai.movielib.R;
import com.ohadshai.movielib.ui.UIConsts;
import com.ohadshai.movielib.ui.fragments.InTheatersFragment;
import com.ohadshai.movielib.ui.fragments.MyActorsFragment;
import com.ohadshai.movielib.ui.fragments.MyDirectorsFragment;
import com.ohadshai.movielib.ui.fragments.MyMoviesFragment;
import com.ohadshai.movielib.ui.fragments.PopularMoviesFragment;
import com.ohadshai.movielib.ui.fragments.UpcomingMoviesFragment;
import com.ohadshai.movielib.ui.fragments.WishlistFragment;
import com.ohadshai.movielib.utils.AdMobConsts;
import com.ohadshai.movielib.utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //region Private Members

    /**
     * Holds the {@link FloatingActionButton} for adding action.
     */
    private FloatingActionButton fabAdd;

    /**
     * Holds the {@link NavigationView} control for the sidebar menu.
     */
    private NavigationView navigationView;

    //endregion

    //region Activity Events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initControls();

        if (savedInstanceState == null)
            this.firstInitControls(); // Initializes controls for the first load.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSettings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handles navigation view item clicks:
        int id = item.getItemId();

        if (id == R.id.navInTheaters) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flMainContent, new InTheatersFragment(), UIConsts.Fragments.IN_THEATERS_FRAGMENT_TAG).commit();
        } else if (id == R.id.navPopularMovies) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flMainContent, new PopularMoviesFragment(), UIConsts.Fragments.POPULAR_MOVIES_FRAGMENT_TAG).commit();
        } else if (id == R.id.navUpcomingMovies) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flMainContent, new UpcomingMoviesFragment(), UIConsts.Fragments.UPCOMING_MOVIES_FRAGMENT_TAG).commit();
        } else if (id == R.id.navWishlist) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flMainContent, new WishlistFragment(), UIConsts.Fragments.WISHLIST_FRAGMENT_TAG).commit();
        } else if (id == R.id.navMyMovies) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flMainContent, new MyMoviesFragment(), UIConsts.Fragments.MY_MOVIES_FRAGMENT_TAG).commit();
        } else if (id == R.id.navMyActors) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flMainContent, new MyActorsFragment(), UIConsts.Fragments.MY_ACTORS_FRAGMENT_TAG).commit();
        } else if (id == R.id.navMyDirectors) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flMainContent, new MyDirectorsFragment(), UIConsts.Fragments.MY_DIRECTORS_FRAGMENT_TAG).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Closes the navigation drawer if it's open:
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        //region Handles back press in fragments owned by the activity:

        InTheatersFragment inTheatersFragment = (InTheatersFragment) getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.IN_THEATERS_FRAGMENT_TAG);
        if (inTheatersFragment != null)
            if (inTheatersFragment.onBackPressed())
                return;

        PopularMoviesFragment popularMoviesFragment = (PopularMoviesFragment) getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.POPULAR_MOVIES_FRAGMENT_TAG);
        if (popularMoviesFragment != null)
            if (popularMoviesFragment.onBackPressed())
                return;

        UpcomingMoviesFragment upcomingMoviesFragment = (UpcomingMoviesFragment) getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.UPCOMING_MOVIES_FRAGMENT_TAG);
        if (upcomingMoviesFragment != null)
            if (upcomingMoviesFragment.onBackPressed())
                return;

        WishlistFragment wishlistFragment = (WishlistFragment) getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.WISHLIST_FRAGMENT_TAG);
        if (wishlistFragment != null)
            if (wishlistFragment.onBackPressed())
                return;

        MyMoviesFragment myMoviesFragment = (MyMoviesFragment) getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.MY_MOVIES_FRAGMENT_TAG);
        if (myMoviesFragment != null)
            if (myMoviesFragment.onBackPressed())
                return;

        MyActorsFragment myActorsFragment = (MyActorsFragment) getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.MY_ACTORS_FRAGMENT_TAG);
        if (myActorsFragment != null)
            if (myActorsFragment.onBackPressed())
                return;

        MyDirectorsFragment myDirectorsFragment = (MyDirectorsFragment) getSupportFragmentManager().findFragmentByTag(UIConsts.Fragments.MY_DIRECTORS_FRAGMENT_TAG);
        if (myDirectorsFragment != null)
            if (myDirectorsFragment.onBackPressed())
                return;

        //endregion

        super.onBackPressed();
    }

    //endregion

    //region Public API

    /**
     * Gets the {@link FloatingActionButton} control "fabAdd".
     *
     * @return Returns the {@link FloatingActionButton} control "fabAdd".
     */
    public FloatingActionButton getFabAdd() {
        return fabAdd;
    }

    //endregion

    //region Private Methods

    /**
     * Initializes all view controls.
     */
    private void initControls() {

        // Loads the default values from the preferences file:
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.settings, true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.hide();

        //region Google AdMob Banner

        //// Initializes the Google AdMob App Id:
        //MobileAds.initialize(this, AdMobConsts.APP_ID);

        //// Sets the Google AdMob banner (bottom):
        //AdView adViewMainBottom = findViewById(R.id.adViewMainBottom);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //adViewMainBottom.loadAd(adRequest);

        //endregion

    }

    /**
     * First initializations for controls.
     */
    private void firstInitControls() {

        //// Logs the user information, when enters the application:
        //Utils.UserLog.log(this, Utils.UserLog.LOG_ON_ENTER);

        navigationView.setCheckedItem(R.id.navInTheaters); // Checks the "In Theaters" item in the sidebar menu.

        // Loads the default fragment "In Theaters":
        getSupportFragmentManager().beginTransaction().replace(R.id.flMainContent, new InTheatersFragment(), UIConsts.Fragments.IN_THEATERS_FRAGMENT_TAG).commit();

    }

    //endregion

}
