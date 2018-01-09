package com.cragchat.mobile.ui.presenter;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.view.activity.RouteActivity;
import com.cragchat.mobile.ui.view.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.util.FormatUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteActivityPresenter implements AppBarLayout.OnOffsetChangedListener {

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

    private RouteActivity activity;

    public RouteActivityPresenter(RouteActivity activity, String routeKey, int tab) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        collapsingToolbarLayout.setTitleEnabled(false);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout.addOnOffsetChangedListener(this);

        final RouteActivityPagerAdapter pageAdapter = new RouteActivityPagerAdapter(activity,
                activity.getSupportFragmentManager(), appBarLayout, floatingActionButton, routeKey);

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
        pager.setCurrentItem(tab);
        pager.addOnPageChangeListener(pageAdapter);

        tabs.setupWithViewPager(pager);
    }

    public void present(Route route) {
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

        activity.getSupportActionBar().setTitle(route.getName());

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

        activity.getSupportActionBar().setSubtitle(subTitle.toString());
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