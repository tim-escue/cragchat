package com.cragchat.mobile.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.mvp.view.adapters.ElevationTransformer;
import com.cragchat.mobile.mvp.view.adapters.pager.MainPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends SearchableActivity {

    private static final String[] INITIAL_AREAS = new String[]{"74c9deb6ef9b4820a5f039eb16ab1193"};//, "870dc2efc04d43c39b74c564c4bca5cf"};

    @BindView(R.id.navigation)
    NavigationView navigationView;
    @BindView(R.id.view_pager)
    ViewPager pager;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private TextView navigationTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupActionBar();

        setupNavigationDrawer();

        final MainPagerAdapter adapter = new MainPagerAdapter(this);
        pager.setAdapter(adapter);
        ElevationTransformer transformer = new ElevationTransformer(pager, adapter);
        pager.addOnPageChangeListener( transformer);

        repository.observeAreas(INITIAL_AREAS).subscribe(adapter::setData);

    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    protected void setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void setupNavigationDrawer() {
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

        //mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setupNavMenu();
        navigationView.getMenu().getItem(0).setOnMenuItemClickListener(
                menuItem -> {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    mDrawerLayout.closeDrawer(Gravity.START);
                    menuItem.setChecked(false);
                    return true;
                }
        );

        navigationView.getMenu().getItem(1).setOnMenuItemClickListener(
                menuItem -> {
                    authentication.logout(MainActivity.this);
                    setupNavMenu();
                    mDrawerLayout.closeDrawer(Gravity.START);
                    menuItem.setChecked(false);
                    return true;
                }
        );

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
    public void onPostCreate(Bundle savedInstanceState) {
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

}



