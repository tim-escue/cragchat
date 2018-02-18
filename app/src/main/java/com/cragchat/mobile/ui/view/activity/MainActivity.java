package com.cragchat.mobile.ui.view.activity;

import android.os.Bundle;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.view.fragments.CragsFragment;
import com.cragchat.mobile.util.ActivityUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends NavigableActivity {

    private static final String CRAG_FRAGMENT_TAG = "CRAG_FRAGMENT";

    @Inject
    CragsFragment mCragsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_main);

        CragsFragment fragment = (CragsFragment) getSupportFragmentManager()
                .findFragmentByTag(CRAG_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = mCragsFragment;
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.fragment_container, CRAG_FRAGMENT_TAG);
        }
    }

}
