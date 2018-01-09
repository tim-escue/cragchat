package com.cragchat.mobile.ui.presenter;

import android.support.v7.widget.Toolbar;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.view.activity.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivityPresenter {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public ProfileActivityPresenter(ProfileActivity activity, String username) {
        ButterKnife.bind(this, activity);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(username);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}