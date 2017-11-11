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
import com.cragchat.mobile.model.realm.RealmArea;
import com.cragchat.mobile.model.realm.RealmRoute;
import com.cragchat.mobile.util.FormatUtil;
import com.cragchat.mobile.view.adapters.pager.RouteActivityPagerAdapter;
import com.cragchat.mobile.view.adapters.pager.TabPagerAdapter;

import io.realm.Realm;

public class RouteActivity extends SearchableActivity implements AppBarLayout.OnOffsetChangedListener {

    private FloatingActionButton floatingActionButton;
    private RealmRoute route;
    private LinearLayout header;
    private Realm mRealm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        floatingActionButton = findViewById(R.id.add_button);
        mRealm = Realm.getDefaultInstance();

        String displayableString = getIntent().getStringExtra(CragChatActivity.DATA_STRING);
        route = mRealm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, displayableString).findFirst();

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
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
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

        Area current = mRealm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, route.getParent()).findFirst();
        int count = 0;
        while (current != null) {
            if (count > 0) {
                subTitle.insert(0, " -> ");
            }
            subTitle.insert(0, current.getName());
            count++;
            current = mRealm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, current.getParent()).findFirst();
        }
        getSupportActionBar().setSubtitle(subTitle.toString());

    }

    public int getInitialTabIndex() {
        return getIntent().getIntExtra("TAB", 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            launch(mRealm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, route.getParent()).findFirst());
        }
        return super.onOptionsItemSelected(item);
    }

}
