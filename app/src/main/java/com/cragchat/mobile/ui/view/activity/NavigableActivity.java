package com.cragchat.mobile.ui.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;

public class NavigableActivity extends SearchableActivity {

    NavigationView navigationView;
    TextView navigationTitle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mRootView;
    private ActionBarDrawerToggle mDrawerToggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigable);
        mRootView = findViewById(R.id.layout_root);

        setupActionBar();

        setupNavigationDrawer();
    }

    @Override
    int getToolbarColor() {
        return ContextCompat.getColor(this, R.color.primary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void addContent(@LayoutRes int layout) {
        getLayoutInflater().inflate(layout, mRootView, true);
    }

    protected void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    protected void setupNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                R.string.drawer_closed) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        // mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView = findViewById(R.id.navigation);
        setupNavMenu();
        navigationView.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(NavigableActivity.this, LoginActivity.class));
                return true;
            }
        });

        navigationView.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                authentication.logout(NavigableActivity.this);
                setupNavMenu();
                mDrawerLayout.closeDrawer(Gravity.START);
                navigationTitle.setText(("CragChat"));
                return true;
            }
        });

        navigationTitle = navigationView.getHeaderView(0).findViewById(R.id.navigation_view_title);
        if (authentication.isLoggedIn(this)) {
            navigationTitle.setText(authentication.getAuthenticatedUser(this).getName());
        }
    }

    public void setupNavMenu() {
        if (authentication.isLoggedIn(this)) {
            navigationView.getMenu().getItem(0).setVisible(false);
            navigationView.getMenu().getItem(1).setVisible(true);
        } else {
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(0).setVisible(true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.more) {
            openPopupMenu(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

}
