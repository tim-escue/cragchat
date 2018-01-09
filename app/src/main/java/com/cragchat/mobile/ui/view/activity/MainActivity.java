package com.cragchat.mobile.ui.view.activity;

import android.os.Bundle;

import com.cragchat.mobile.R;

public class MainActivity extends NavigableActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addContent(R.layout.activity_main);
    }

}
