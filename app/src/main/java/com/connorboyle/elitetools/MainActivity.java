package com.connorboyle.elitetools;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.connorboyle.elitetools.fragments.BearingCalculator;
import com.connorboyle.elitetools.fragments.BlueprintsActivity;
import com.connorboyle.elitetools.fragments.EngineersActivity;
import com.connorboyle.elitetools.fragments.HabZoneCalculator;
import com.connorboyle.elitetools.fragments.HomeView;
import com.connorboyle.elitetools.fragments.MaterialsFinderActivity;
import com.connorboyle.elitetools.fragments.Notebook;
import com.connorboyle.elitetools.fragments.SystemFinder;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContent, HomeView.class.newInstance())
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDrawerContent(NavigationView nv) {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onDrawerItemSelected(item);
                return true;
            }
        });
    }

    private void onDrawerItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragClass = null;
        switch (item.getItemId()) {
            case R.id.nav_bearing:
                fragClass = BearingCalculator.class;
                break;
            case R.id.nav_hzc:
                fragClass = HabZoneCalculator.class;
                break;
            case R.id.nav_materials:
                fragClass = MaterialsFinderActivity.class;
                break;
            case R.id.nav_notes:
                fragClass = Notebook.class;
                break;
            case R.id.nav_blueprints:
                fragClass = BlueprintsActivity.class;
                break;
            case R.id.nav_engineers:
                fragClass = EngineersActivity.class;
                break;
            case R.id.nav_home:
                fragClass = HomeView.class;
                break;
            case R.id.nav_systems:
                fragClass = SystemFinder.class;
                break;
            case R.id.nav_ext_reddit:
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/r/elitedangerous/"));
                startActivity(browserIntent);
                break;
        }

        try {
            fragment = (Fragment) fragClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.flContent, fragment).commit();
            item.setChecked(true);
            setTitle(item.getTitle());
            mDrawer.closeDrawers();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
