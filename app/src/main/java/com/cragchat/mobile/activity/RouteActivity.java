package com.cragchat.mobile.activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.util.FormatUtil;
import com.cragchat.mobile.view.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.view.adapters.pager.TabPagerAdapter;

public class RouteActivity extends SearchableActivity implements AppBarLayout.OnOffsetChangedListener {

    private FloatingActionButton floatingActionButton;
    private Route route;
    private LinearLayout header;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        floatingActionButton = findViewById(R.id.add_button);

        String displayableString = getIntent().getStringExtra(CragChatActivity.DATA_STRING);
        route = Repository.getRoute(displayableString, null);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        setupToolbar();

        header = findViewById(R.id.header);

        TextView textView = (TextView) findViewById(R.id.type);
        textView.setText(route.getType());

        textView = (TextView) findViewById(R.id.yds_scale);
        String yds = FormatUtil.getYdsString(this, route.getYds());
        textView.setText(yds);

        textView = (TextView) findViewById(R.id.stars);
        String sters = FormatUtil.getStarsString(route.getStars());
        textView.setText(sters);


        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(this);

        final RouteActivityPagerAdapter pageAdapter = new RouteActivityPagerAdapter(this,
                getSupportFragmentManager(), appBarLayout, floatingActionButton, route);
        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);

        setAddButtonPagerAndAdapter(pager, pageAdapter);

        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(getInitialTabIndex());
        pager.addOnPageChangeListener(pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.tabs);
        slab.setupWithViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percenStringe = (float) Math.abs(offset) / (float) maxScroll;
        float alpha = 1f - percenStringe;
        header.setAlpha(alpha);
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
            launch(Repository.getArea(route.getParent(), null));
        }
        return super.onOptionsItemSelected(item);
    }

}
