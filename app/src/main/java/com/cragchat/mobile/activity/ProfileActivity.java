package com.cragchat.mobile.activity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.cragchat.mobile.R;
import com.cragchat.mobile.view.adapters.pager.ProfilePagerAdapter;
import com.cragchat.mobile.search.NavigableActivity;

public class ProfileActivity extends NavigableActivity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_profile);
        String username = getIntent().getStringExtra("username");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(username);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        ProfilePagerAdapter pageAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), username);
        pager.setAdapter(pageAdapter);

        TabLayout slab = (TabLayout) findViewById(R.id.sliding_tabs);
        slab.setupWithViewPager(pager);
    }


}


