package com.cragchat.mobile.search;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.LoginActivity;
import com.cragchat.mobile.activity.MainActivity;
import com.cragchat.mobile.activity.ProfileActivity;
import com.cragchat.mobile.view.adapters.recycler.SearchResultRecyclerAdapter;
import com.cragchat.mobile.sql.SearchQueryTaskNew;
import com.cragchat.mobile.user.User;

public class NavigableActivity extends CragChatActivity {

    public static final String USE_HOME_ICON = "USE_HOME_ICON";

    private DrawerLayout mDrawerLayout;
    private LinearLayout mRootView;
    private RecyclerView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean useHomeButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigable);
        mRootView = findViewById(R.id.layout_root);
        useHomeButton = getIntent().getBooleanExtra(USE_HOME_ICON, true);

        setupNavigationDrawer();
    }

    protected void addContent(@LayoutRes int layout) {
        getLayoutInflater().inflate(layout, mRootView, true);
    }

    protected void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    protected void setupNavigationDrawer() {
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(llm);

        mDrawerList.setAdapter(new SearchResultRecyclerAdapter(this, mDrawerList));
        new SearchQueryTaskNew(this, mDrawerList).execute("");

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

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        TextInputEditText editText = (TextInputEditText) findViewById(R.id.search_edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                new SearchQueryTaskNew(NavigableActivity.this, mDrawerList).execute(s.toString());
            }
        });


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupActionBar();
        if (useHomeButton) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (useHomeButton) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (useHomeButton && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.search) {
            if (getDrawerLayout().isDrawerOpen(Gravity.START)) {
                getDrawerLayout().closeDrawer(Gravity.START);
            } else {
                getDrawerLayout().openDrawer(Gravity.START);
            }
        } else if (item.getItemId() == R.id.more) {
            launchProfile(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean launchProfile(MenuItem menuItem) {
        int layout = User.currentToken(this) != null ? R.menu.menu_profile : R.menu.menu_not_logged_in;
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
                    intent.putExtra("username", User.userName(NavigableActivity.this));
                    break;
                case "Log out":
                    User.logout(NavigableActivity.this);
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
