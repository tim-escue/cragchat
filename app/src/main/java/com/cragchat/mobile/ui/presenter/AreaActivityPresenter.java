package com.cragchat.mobile.ui.presenter;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.view.activity.AreaActivity;
import com.cragchat.mobile.ui.view.adapters.pager.AreaActivityPagerAdapter;
import com.cragchat.mobile.ui.view.adapters.pager.TabPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by timde on 1/4/2018.
 */

public class AreaActivityPresenter implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.header)
    View header;
    @BindView(R.id.viewpager)
    ViewPager pager;
    @BindView(R.id.add_button)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.areas)
    TextView areas;
    @BindView(R.id.routes)
    TextView routes;
    @BindView(R.id.images)
    TextView images;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AreaActivity areaActivity;

    public AreaActivityPresenter(AreaActivity activity, Area area) {
        this.areaActivity = activity;
        ButterKnife.bind(this, activity);
        collapsingToolbarLayout.setTitleEnabled(false);
        appBarLayout.addOnOffsetChangedListener(this);
        areaActivity.setSupportActionBar(toolbar);
        areaActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AreaActivityPagerAdapter pageAdapter = new AreaActivityPagerAdapter(areaActivity,
                areaActivity.getSupportFragmentManager(), appBarLayout, area, floatingActionButton);

        pager.setAdapter(pageAdapter);
        pager.addOnPageChangeListener(pageAdapter);
        pager.setCurrentItem(areaActivity.getInitialTabIndex());
        setAddButtonPagerAndAdapter(pager, pageAdapter);

        tabLayout.setupWithViewPager(pager);
    }

    public void present(Area area) {
        areaActivity.getSupportActionBar().setTitle(area.getName());

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
            areaActivity.getSupportActionBar().setSubtitle(subTitle.toString());
        }

        images.setText(String.valueOf(area.getImages().size()));
        routes.setText(String.valueOf(area.getRoutes().size()));
        areas.setText(String.valueOf(area.getSubAreas().size()));
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

    public void switchTab(int tab) {
        pager.setCurrentItem(tab, true);
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
}
