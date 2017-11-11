package com.cragchat.mobile.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;

public class NavigableActivity extends CragChatActivity {

    public static final String USE_HOME_ICON = "USE_HOME_ICON";

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

    protected void addContent(@LayoutRes int layout) {
        getLayoutInflater().inflate(layout, mRootView, true);
    }

    protected void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
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

    public boolean openPopupMenu(MenuItem menuItem) {
        int layout = Authentication.isLoggedIn(this) ? R.menu.menu_profile : R.menu.menu_not_logged_in;
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.more));
        popup.getMenuInflater().inflate(layout, popup.getMenu());
        popup.setOnMenuItemClickListener(menuListener);
        popup.show();
        return true;
    }

    PopupMenu.OnMenuItemClickListener menuListener = new PopupMenu.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = null;
            String selection = item.getTitle().toString();
            switch (selection) {
                case "My Profile":
                    intent = new Intent(NavigableActivity.this, ProfileActivity.class);
                  //  intent.putExtra("username", User.userName(NavigableActivity.this));
                    break;
                case "Log out":
                   // User.logout(NavigableActivity.this);
                    intent = new Intent(NavigableActivity.this, MainActivity.class);
                    break;
                case "Login":
                    intent = new Intent(NavigableActivity.this, LoginActivity.class);

                    break;
            }
            if (intent != null) {
                startActivity(intent);
            }
            return true;
        }
    };

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
