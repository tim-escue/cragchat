package com.cragchat.mobile.ui.view.activity;

import android.os.Bundle;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.view.fragments.CragsFragment;
import com.cragchat.mobile.util.ActivityUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends NavigableActivity {

    @Inject
    CragsFragment mCragsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_main);

        CragsFragment fragment = (CragsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = mCragsFragment;
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.fragment_container);
        }
    }

}
