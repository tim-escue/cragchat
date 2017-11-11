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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cragchat.mobile.R;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.view.adapters.pager.AreaActivityPagerAdapter;
import com.cragchat.mobile.view.adapters.pager.TabPagerAdapter;

public class AreaActivity extends SearchableActivity {

    private Area area;
    private FloatingActionButton floatingActionButton;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_displayable_new);

        floatingActionButton = findViewById(R.id.add_button);

        String areaKey = getIntent().getStringExtra(CragChatActivity.DATA_STRING);
        area = Repository.getArea(areaKey, this);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        setupToolbar();

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);

        AreaActivityPagerAdapter pageAdapter = new AreaActivityPagerAdapter(this,
                getSupportFragmentManager(), appBarLayout, area, floatingActionButton);

        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        pager.addOnPageChangeListener(pageAdapter);
        pager.setCurrentItem(getInitialTabIndex());
        setAddButtonPagerAndAdapter(pager, pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.tabs);
        slab.setupWithViewPager(pager);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public int getInitialTabIndex() {
        return getIntent().getIntExtra("TAB", 0);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(area.getName());

        StringBuilder subTitle = new StringBuilder();
        Area current = Repository.getArea(area.getParent(), null);
        int count = 0;
        while (current != null) {
            if (count > 0) {
                subTitle.insert(0, " -> ");
            }
            subTitle.insert(0, current.getName());
            count++;
            current = Repository.getArea(current.getParent(), null);
        }
        String subTitleString = subTitle.toString();
        if (!subTitleString.isEmpty()) {
            getSupportActionBar().setSubtitle(subTitle.toString());
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Area parent = Repository.getArea(area.getParent(), null);
            if (parent != null) {
                launch(parent);
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }


}