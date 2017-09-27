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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.view.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.view.adapters.pager.TabPagerAdapter;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.mobile.sql.LocalDatabase;

import org.json.JSONObject;

public class RouteActivity extends NavigableActivity implements AppBarLayout.OnOffsetChangedListener {

    private FloatingActionButton floatingActionButton;
    private Route route;
    private LinearLayout header;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_route);

        floatingActionButton = findViewById(R.id.add_button);

        String displayableString = getIntent().getStringExtra(CragChatActivity.DATA_STRING);
        try {
            route = Displayable.decodeRoute(
                    new JSONObject(displayableString));
        } catch (Exception e) {
            Log.e("DisplayableActivity", "Could not decode route");
            e.printStackTrace();
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        /*collapsingToolbarLayout.setTitle(area.getName());
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white)); */
        // collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.primary));
        collapsingToolbarLayout.setTitleEnabled(false);

        setupToolbar();

        header = findViewById(R.id.header);

        TextView textView = (TextView) findViewById(R.id.type);
        textView.setText(route.getType());

        textView = (TextView) findViewById(R.id.yds_scale);
        String yds = route.getYdsString(this, route.getYds(this));
        textView.setText(yds);

        textView = (TextView) findViewById(R.id.stars);
        String sters = route.getStarsString(this);
        textView.setText(sters);


        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(this);

        /*ImageView routeImage = findViewById(R.id.route_image);
        routeImage.setImageResource(route.getType().equalsIgnoreCase("sport") ?
                R.drawable.bolt_img : R.drawable.nuts);*/

        final RouteActivityPagerAdapter pageAdapter = new RouteActivityPagerAdapter(this,
                getSupportFragmentManager(), appBarLayout, floatingActionButton, route.getId());
        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);

        setAddButtonPagerAndAdapter(pager, pageAdapter);

        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(getInitialTabIndex());
        pager.addOnPageChangeListener(pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.tabs);
        slab.setupWithViewPager(pager);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;
        float alpha = 1f - percentage;
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
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(route.getName());
        getSupportActionBar().setSubtitle(route.getSubTitle(this));

    }

    public int getInitialTabIndex() {
        return getIntent().getIntExtra("TAB", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Displayable parent = LocalDatabase.getInstance(this).getParent(route);
            if (parent != null) {
                launch(parent);
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        openDisplayable(v);
    }

}
