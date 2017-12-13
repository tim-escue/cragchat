package com.cragchat.mobile.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.cragchat.mobile.R;

public class ProfileActivity extends NavigableActivity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_profile);
        String username = getIntent().getStringExtra("username");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(username);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }


}


