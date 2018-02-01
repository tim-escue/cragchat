package com.cragchat.mobile.ui.view.activity;

import android.os.Bundle;

import com.cragchat.mobile.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends NavigableActivity {

    @Inject
    String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        addContent(R.layout.activity_main);
        System.out.println("FROM MAIN:" + message);
    }

}
