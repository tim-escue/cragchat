package com.cragchat.mobile.mvp.presenter;

import android.content.Intent;
import android.view.View;

import com.cragchat.mobile.R;
import com.cragchat.mobile.mvp.view.activity.MapActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationFragmentPresenter {

    @OnClick(R.id.show_map)
    void showMap(View view) {
        Intent intent = new Intent(view.getContext(), MapActivity.class);
        view.getContext().startActivity(intent);
    }

    public LocationFragmentPresenter(View view) {
        ButterKnife.bind(this, view);
    }
}