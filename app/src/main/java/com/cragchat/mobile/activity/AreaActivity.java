package com.cragchat.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
        addContent(R.layout.activity_area);

        area = Displayable.decodeAreaString(getIntent().getStringExtra(CragChatActivity.DATA_STRING));

        getSupportActionBar().setTitle(area.getName());
        getSupportActionBar().setSubtitle(area.getSubTitle(this));

        AreaActivityPagerAdapter pageAdapter = new AreaActivityPagerAdapter(this, getSupportFragmentManager(), area);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.sliding_tabs);
        slab.setupWithViewPager(pager);

        TextView numRoutesView = (TextView) findViewById(R.id.num_routes);
        int numRouutes = LocalDatabase.getInstance(this).findRoutesWithin(area).size();
        numRoutesView.setText(numRouutes + " routes");
    }

    @Override
    public Displayable getDisplayable() {
        return area;
    }

    public void onClick(View v) {
        openDisplayable(v);
    }
}