package com.cragchat.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.cragchat.mobile.R;
import com.cragchat.mobile.view.adapters.pager.TabPagerAdapter;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.mobile.sql.LocalDatabase;

import org.json.JSONObject;

/**
 * Created by timde on 8/31/2017.
 */

public abstract class DisplayableActivity extends NavigableActivity {

    private RelativeLayout headerContainer;
    private FloatingActionButton floatingActionButton;
    private Displayable displayable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_displayable);
        //addContent(R.layout.activity_displayable_new);
        headerContainer = findViewById(R.id.app_bar_layout);
        floatingActionButton = findViewById(R.id.add_button);

        getDisplayableFromIntent();

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public int getInitialTabIndex() {
        return getIntent().getIntExtra("TAB", 0);
    }

    private void getDisplayableFromIntent() {
        String displayableString = getIntent().getStringExtra(CragChatActivity.DATA_STRING);
        if (displayableString.startsWith("AREA")) {
            displayable = Displayable.decodeAreaString(displayableString);
        } else {
            try {
                displayable = Displayable.decodeRoute(
                        new JSONObject(displayableString));
            } catch (Exception e) {
                Log.e("DisplayableActivity", "Could not decode route");
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Displayable parent = LocalDatabase.getInstance(this).getParent(getDisplayable());
            if (parent != null) {
                launch(parent);
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void setAddButtonPagerAndAdapter(final ViewPager pager, final TabPagerAdapter pageAdapter) {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = pageAdapter.getItem(pager.getCurrentItem());
                if (fragment instanceof View.OnClickListener) {
                    View.OnClickListener clickListener = (View.OnClickListener) fragment;
                    clickListener.onClick(view);
                }
            }
        });
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }

    public Displayable getDisplayable() {
        return displayable;
    }


}
