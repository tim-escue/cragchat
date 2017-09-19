package com.cragchat.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.pager.AreaActivityPagerAdapter;
import com.cragchat.mobile.adapters.pager.TabPagerAdapter;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.mobile.sql.LocalDatabase;

public class AreaActivity extends NavigableActivity {

    private Area area;

    private FloatingActionButton floatingActionButton;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        addContent(R.layout.activity_displayable_new);

        floatingActionButton = findViewById(R.id.add_button);

        area = Displayable.decodeAreaString(getIntent().getStringExtra(CragChatActivity.DATA_STRING));
        area.loadStatistics(this);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        setupToolbar();

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);

        AreaActivityPagerAdapter pageAdapter = new AreaActivityPagerAdapter(this,
                getSupportFragmentManager(), appBarLayout, area, area.getSubAreas(), area.getRoutes(),  floatingActionButton);

        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        pager.addOnPageChangeListener(pageAdapter);
        pager.setCurrentItem(getInitialTabIndex());
        setAddButtonPagerAndAdapter(pager, pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.tabs);
        slab.setupWithViewPager(pager);

    }

    public int getInitialTabIndex() {
        return getIntent().getIntExtra("TAB", 0);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(area.getName());
       getSupportActionBar().setSubtitle(area.getSubTitle(this));

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

    public void onClick(View v) {
        openDisplayable(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Displayable parent = LocalDatabase.getInstance(this).getParent(area);
            if (parent != null) {
                launch(parent);
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}