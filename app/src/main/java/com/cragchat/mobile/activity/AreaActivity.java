package com.cragchat.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.AreaActivityPagerAdapter;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.search.SearchableActivity;
import com.cragchat.mobile.sql.LocalDatabase;

public class AreaActivity extends SearchableActivity {
    private Area area;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_area);

        area = Displayable.decodeAreaString(getIntent().getStringExtra(CragChatActivity.DATA_STRING));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(area.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Area[] hierarchy = LocalDatabase.getInstance(this).getHierarchy(area);

        LinearLayout navLayout = (LinearLayout) findViewById(R.id.nav_layout);
        for (int i = 0 ; i < hierarchy.length; i++) {
            LinearLayout button = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_nav_button, null);
            if (i == 0) {
                TextView arrow = (TextView) button.findViewById(R.id.text_arrow);
                arrow.setVisibility(View.GONE);
            }
            TextView location = (TextView) button.findViewById(R.id.text_location);
            location.setText(hierarchy[i].getName());
            navLayout.addView(button);
        }


        AreaActivityPagerAdapter pageAdapter = new AreaActivityPagerAdapter(this, getSupportFragmentManager(), area);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.sliding_tabs);
        slab.setupWithViewPager(pager);

        TextView numRoutesView = (TextView) findViewById(R.id.num_routes);
        int numRouutes = LocalDatabase.getInstance(this).findRoutesWithin(area).size();
        numRoutesView.setText(numRouutes + " routes");
    }

    public void onClick(View v) {
        openDisplayable(v);
    }
}