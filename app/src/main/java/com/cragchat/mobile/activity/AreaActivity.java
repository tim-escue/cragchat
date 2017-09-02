package com.cragchat.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.pager.AreaActivityPagerAdapter;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.sql.LocalDatabase;

public class AreaActivity extends DisplayableActivity {

    private Area area;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.collapsing_tab_toolbar);

        int tabInd = getIntent().getIntExtra("TAB", 0);
        area = Displayable.decodeAreaString(getIntent().getStringExtra(CragChatActivity.DATA_STRING));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setTitle(area.getName());
       // getSupportActionBar().setSubtitle(area.getSubTitle(this));

        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        AreaActivityPagerAdapter pageAdapter = new AreaActivityPagerAdapter(this, getSupportFragmentManager(), appBarLayout, area);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        pager.addOnPageChangeListener(pageAdapter);
        pager.setCurrentItem(tabInd);

        TabLayout slab = (TabLayout) findViewById(R.id.tabs);
        slab.setupWithViewPager(pager);

        //TextView numRoutesView = (TextView) findViewById(R.id.num_routes);
       // int numRouutes = LocalDatabase.getInstance(this).findRoutesWithin(area).size();
       // numRoutesView.setText(numRouutes + " routes");
    }

    @Override
    public Displayable getDisplayable() {
        return area;
    }

    public void onClick(View v) {
        openDisplayable(v);
    }
}