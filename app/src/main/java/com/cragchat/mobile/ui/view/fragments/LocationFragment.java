package com.cragchat.mobile.ui.view.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.ui.presenter.LocationFragmentPresenter;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;

public class LocationFragment extends DaggerFragment implements View.OnClickListener {

    @Inject
    CommentSectionFragment commentFragment;

    @Inject
    public LocationFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        if (getChildFragmentManager().findFragmentByTag("comment_table")==null) {
            commentFragment.setTable(CommentSectionFragment.TABLE_LOCATION);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_location, commentFragment, "comment_table");
            transaction.commit();
        }

        LocationFragmentPresenter presenter = new LocationFragmentPresenter(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (commentFragment != null) {
            commentFragment.onClick(view);
        }
    }
}