package com.cragchat.mobile.ui.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Send;
import com.cragchat.mobile.ui.presenter.SendFragmentPresenter;
import com.cragchat.mobile.ui.view.activity.SubmitSendActivity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;


public class SendsFragment extends DaggerFragment implements View.OnClickListener {

    @Inject
    Repository repository;

    @Inject
    @Named(InjectionNames.ENTITY_KEY)
    String mEntityKey;

    @Inject
    Authentication mAuthentication;

    private SendFragmentPresenter presenter;

    @Inject
    public SendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_sends, container, false);

        /*
            Called only to update sends. The return value is not used because SendRecyclerAdapter
            relies upon an instance of Realm and repository.can not expose Realm API in order to
            preserve CLEAN architecture.
         */
        presenter = new SendFragmentPresenter(view, getLifecycle(), mEntityKey);
        List<Send> sends = repository.getSends(mEntityKey, new Callback<List<Send>>() {
            @Override
            public void onSuccess(List<Send> object) {
                presenter.present(object);
            }

            @Override
            public void onFailure() {

            }
        });
        presenter.present(sends);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (mAuthentication.isLoggedIn(getContext())) {
                Intent next = new Intent(getContext(), SubmitSendActivity.class);
                next.putExtra("entityKey", mEntityKey);
                startActivity(next);
            } else {
                DialogFragment df = NotificationDialog.newInstance("Must be logged in to add a send.");
                df.show(getFragmentManager(), "dialog");
            }
        }
    }
}
