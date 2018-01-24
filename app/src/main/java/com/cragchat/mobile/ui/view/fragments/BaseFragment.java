package com.cragchat.mobile.ui.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.view.activity.CragChatActivity;

import javax.inject.Inject;

/**
 * Created by timde on 1/24/2018.
 */

public class BaseFragment extends Fragment {

    @Inject
    public Repository repository;

    @Inject
    public Authentication mAuthentication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((CragChatActivity) getActivity()).getRepositoryComponent().inject(this);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

}
