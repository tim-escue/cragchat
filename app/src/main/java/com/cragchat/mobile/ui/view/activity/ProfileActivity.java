package com.cragchat.mobile.ui.view.activity;


import android.os.Bundle;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.presenter.ProfileActivityPresenter;

public class ProfileActivity extends NavigableActivity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_profile);
        String username = getIntent().getStringExtra("username");
        ProfileActivityPresenter presenter = new ProfileActivityPresenter(this, username);
    }


}


