package com.cragchat.mobile.ui.view.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.cragchat.mobile.R;

import butterknife.BindView;

public class ProfileActivity extends NavigableActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_profile);
        String username = getIntent().getStringExtra("username");
        
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(username);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);    }


}


