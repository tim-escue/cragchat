package com.cragchat.mobile.mvp.view.activity;

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
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.mvp.contract.AreaContract;
import com.cragchat.mobile.domain.model.Area;
import com.cragchat.mobile.mvp.view.adapters.pager.AreaActivityPagerAdapter;
import com.cragchat.mobile.mvp.view.adapters.pager.TabPagerAdapter;
import com.cragchat.mobile.mvp.view.fragments.AreaListFragment;
import com.cragchat.mobile.mvp.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.mvp.view.fragments.ImageFragment;
import com.cragchat.mobile.mvp.view.fragments.LocationFragment;
import com.cragchat.mobile.mvp.view.fragments.RecentActivityFragment;
import com.cragchat.mobile.mvp.view.fragments.RouteListFragment;
import com.cragchat.mobile.util.ViewUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaActivity extends SearchableCollapsableActivity implements AreaContract.AreaView, AppBarLayout.OnOffsetChangedListener {

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
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subtitle)
    TextView subtitleView;

    @Inject
    AreaListFragment areaListFragment;
    @Inject
    RecentActivityFragment recentActivityFragment;
    @Inject
    Repository repository;
    @Inject
    CommentSectionFragment commentSectionFragment;
    @Inject
    LocationFragment locationFragment;
    @Inject
    ImageFragment imageFragment;
    @Inject
    RouteListFragment routeListFragment;
    @Inject
    AreaContract.AreaPresenter presenter;
    @Inject
    Area area;

    private String subtitle;
    private boolean showTitle;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_displayable_new);
        ButterKnife.bind(this);

        collapsingToolbarLayout.setTitleEnabled(false);
        appBarLayout.addOnOffsetChangedListener(this);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        subtitle = getSubtitle(area);
        getSupportActionBar().setTitle(area.getName());
        getSupportActionBar().setSubtitle(subtitle);
        title.setText(area.getName());
        subtitleView.setText(subtitle);

        commentSectionFragment.setTable(CommentSectionFragment.TABLE_DISCUSSION);
        AreaActivityPagerAdapter pageAdapter = new AreaActivityPagerAdapter(this, areaListFragment,
                recentActivityFragment, commentSectionFragment, imageFragment, routeListFragment,
                locationFragment, getSupportFragmentManager(), appBarLayout, area, floatingActionButton);

        pager.setAdapter(pageAdapter);
        pager.addOnPageChangeListener(pageAdapter);
        pager.setCurrentItem(getInitialTabIndex());
        setAddButtonPagerAndAdapter(pager, pageAdapter);

        tabLayout.setupWithViewPager(pager);

        present(area);
        presenter.updateArea(area.getKey());
    }

    public int getTabForCommentTable(String commentTable) {
        int tab = 0;
        if (area.getSubAreas().size() == 0) {
            if (commentTable.equals(CommentSectionFragment.TABLE_DISCUSSION)) {
                tab = 2;
            } else if (commentTable.equals(CommentSectionFragment.TABLE_LOCATION)) {
                tab = 3;
            }
        } else {
            if (commentTable.equals(CommentSectionFragment.TABLE_DISCUSSION)) {
                tab = 3;
            } else if (commentTable.equals(CommentSectionFragment.TABLE_LOCATION)) {
                tab = 4;
            }
        }
        return tab;
    }

    public int getInitialTabIndex() {
        return getIntent().getIntExtra("TAB", 0);
    }

    @Override
    public int getToolbarColor() {
        return Color.TRANSPARENT;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_collabsable, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!area.getParent().isEmpty()) {
                ViewUtil.launch(this, area.getParent());
            } else {
                ViewUtil.launch(this, MainActivity.class);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String getSubtitle(Area area) {
        StringBuilder subTitle = new StringBuilder();
        Area current = repository.getArea(area.getParent());
        int count = 0;
        while (current != null) {
            if (count > 0) {
                subTitle.insert(0, " -> ");
            }
            subTitle.insert(0, current.getName());
            count++;
            current = repository.getArea(current.getParent());
        }
        return subTitle.toString();
    }

    public void present(Area area) {
        images.setText(String.valueOf(area.getImages().size()));
        routes.setText(String.valueOf(area.getRoutes().size()));
        areas.setText(String.valueOf(area.getSubAreas().size()));
    }

    public void setAddButtonPagerAndAdapter(final ViewPager pager, final TabPagerAdapter pageAdapter) {
        floatingActionButton.setOnClickListener(view -> {
                    Fragment fragment = pageAdapter.getItem(pager.getCurrentItem());
                    if (fragment instanceof View.OnClickListener) {
                        View.OnClickListener clickListener = (View.OnClickListener) fragment;
                        clickListener.onClick(view);
                    }
                }
        );
    }

    public void switchTab(int tab) {
        pager.setCurrentItem(tab, true);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentScrolled = (float) Math.abs(offset) / (float) maxScroll;
        /*if (percentScrolled > .4) {
            header.setVisibility(View.GONE);
        } else {
            if (header.getVisibility() == View.GONE) {
                header.setVisibility(View.VISIBLE);
            }
            float alpha = 1f - percentScrolled;
            header.setAlpha(alpha);
        }*/
        if (!showTitle && percentScrolled > .6) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            showTitle = true;
        } else if (showTitle && percentScrolled <= .4) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            showTitle = false;
        }
    }

}