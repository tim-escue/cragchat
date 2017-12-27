package com.cragchat.mobile.activity;

import android.graphics.Color;
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
import com.cragchat.mobile.binding.RouteHeaderBinding;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.util.NavigationUtil;
import com.cragchat.mobile.view.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.view.adapters.pager.TabPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteActivity extends SearchableActivity implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.header)
    View header;
    @BindView(R.id.viewpager)
    ViewPager pager;
    private FloatingActionButton floatingActionButton;
    private Route route;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        route = (Route) getIntent().getParcelableExtra(NavigationUtil.ENTITY);

        setContentView(R.layout.activity_route_new);
        ButterKnife.bind(this);

        final RouteHeaderBinding headerBinding = new RouteHeaderBinding(header);

        Repository.getRoute(route.getKey(), new Callback<Route>() {
            @Override
            public void onSuccess(Route object) {
                headerBinding.bind(object);
                route = object;
            }

            @Override
            public void onFailure() {

            }
        });

        headerBinding.bind(route);

        floatingActionButton = findViewById(R.id.add_button);

        header = findViewById(R.id.header);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        setupToolbar();

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(this);

        final RouteActivityPagerAdapter pageAdapter = new RouteActivityPagerAdapter(this,
                getSupportFragmentManager(), appBarLayout, floatingActionButton, route);

        setAddButtonPagerAndAdapter(pager, pageAdapter);

        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(getInitialTabIndex());
        pager.addOnPageChangeListener(pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.tabs);
        slab.setupWithViewPager(pager);

    }

    public int getTabForCommentTable(String table) {
        int tab = 0;
        if (table.equals(CommentSectionFragment.TABLE_BETA)) {
            tab = 0;
        } else if (table.equals(CommentSectionFragment.TABLE_LOCATION)) {
            tab = 3;
        }
        return tab;
    }

    public void switchTab(int tab) {
        pager.setCurrentItem(tab, true);
    }

    @Override
    int getToolbarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentScrolled = (float) Math.abs(offset) / (float) maxScroll;
        if (percentScrolled > .4) {
            header.setVisibility(View.GONE);
        } else {
            if (header.getVisibility() == View.GONE) {
                header.setVisibility(View.VISIBLE);
            }
            float alpha = 1f - percentScrolled;
            header.setAlpha(alpha);
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

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(route.getName());
        StringBuilder subTitle = new StringBuilder();

        Area current = Repository.getArea(route.getParent(), null);
        int count = 0;
        while (current != null) {
            if (count > 0) {
                subTitle.insert(0, " -> ");
            }
            subTitle.insert(0, current.getName());
            count++;
            current = Repository.getArea(current.getParent(), null);
        }
        getSupportActionBar().setSubtitle(subTitle.toString());

    }

    public int getInitialTabIndex() {
        return getIntent().getIntExtra("TAB", 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavigationUtil.launch(this, Repository.getArea(route.getParent(), null));
        }
        return super.onOptionsItemSelected(item);
    }

}
