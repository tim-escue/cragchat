package com.cragchat.mobile.ui.view.activity;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.view.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.ui.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.ui.view.fragments.ImageFragment;
import com.cragchat.mobile.ui.view.fragments.LocationFragment;
import com.cragchat.mobile.ui.view.fragments.RatingFragment;
import com.cragchat.mobile.ui.view.fragments.SendsFragment;
import com.cragchat.mobile.util.FormatUtil;
import com.cragchat.mobile.util.NavigationUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteActivity extends SearchableActivity implements AppBarLayout.OnOffsetChangedListener  {

    @BindView(R.id.header)
    View header;
    @BindView(R.id.viewpager)
    ViewPager pager;
    @BindView(R.id.add_button)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.yds)
    TextView yds;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.stars)
    RatingBar stars;
    @BindView(R.id.ratings)
    TextView ratings;
    @BindView(R.id.sends)
    TextView sends;
    @BindView(R.id.images)
    TextView images;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tabs)
    TabLayout tabs;

    @Inject
    Route route;

    @Inject
    CommentSectionFragment commentSectionFragment;

    @Inject
    RatingFragment ratingFragment;

    @Inject
    SendsFragment sendsFragment;

    @Inject
    ImageFragment imageFragment;

    @Inject
    LocationFragment locationFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentSectionFragment.setTable(CommentSectionFragment.TABLE_BETA);

        setContentView(R.layout.activity_route_new);

        ButterKnife.bind(this);

        collapsingToolbarLayout.setTitleEnabled(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout.addOnOffsetChangedListener(this);

        final RouteActivityPagerAdapter pageAdapter = new RouteActivityPagerAdapter(this,
                getSupportFragmentManager(), commentSectionFragment, ratingFragment, sendsFragment,
                imageFragment, locationFragment, appBarLayout, floatingActionButton, route.getKey());

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

        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(getTabForCommentTable(CommentSectionFragment.TABLE_BETA));
        pager.addOnPageChangeListener(pageAdapter);

        tabs.setupWithViewPager(pager);

        repository.getRoute(route.getKey(), new Callback<Route>() {
            @Override
            public void onSuccess(Route object) {
                present(object, repository);
                route = object;
            }

            @Override
            public void onFailure() {

            }
        });

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

    @Override
    public int getToolbarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavigationUtil.launch(this, repository.getArea(route.getParent(), null));
        }
        return super.onOptionsItemSelected(item);
    }

    public void present(Route route, Repository repository) {
        if (!route.getRatings().equalsIgnoreCase("0")) {
            yds.setText(FormatUtil.getYdsString(yds.getContext(), route.getYds()));
        } else {
            yds.setText("-");
        }
        type.setText(route.getRouteType().toString());
        stars.setRating((float) route.getStars());
        ratings.setText(String.valueOf(route.getRatings()));
        sends.setText(String.valueOf(route.getSends()));
        images.setText(String.valueOf(route.getImages().size()));

        getSupportActionBar().setTitle(route.getName());

        StringBuilder subTitle = new StringBuilder();
        Area current = repository.getArea(route.getParent(), null);
        int count = 0;
        while (current != null) {
            if (count > 0) {
                subTitle.insert(0, " -> ");
            }
            subTitle.insert(0, current.getName());
            count++;
            current = repository.getArea(current.getParent(), null);
        }

        getSupportActionBar().setSubtitle(subTitle.toString());
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

    public void switchTab(int tab) {
        pager.setCurrentItem(tab, true);
    }

}

