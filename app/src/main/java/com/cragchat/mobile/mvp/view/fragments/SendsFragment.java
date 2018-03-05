package com.cragchat.mobile.mvp.view.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.data.authentication.Authentication;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.mvp.contract.SendsContract;
import com.cragchat.mobile.domain.model.Send;
import com.cragchat.mobile.mvp.presenter.SendFragmentPresenter;
import com.cragchat.mobile.mvp.view.activity.SubmitSendActivity;
import com.cragchat.mobile.mvp.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.mvp.view.adapters.recycler.SendRecyclerAdapter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;


public class SendsFragment extends DaggerFragment implements View.OnClickListener, SendsContract.SendView {

    @BindView(R.id.list_sends)
    RecyclerView recyclerView;
    @BindView(R.id.list_empty)
    TextView empty;

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

        ButterKnife.bind(this, view);
        SendRecyclerAdapter adapter = SendRecyclerAdapter.create(mEntityKey);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
        getLifecycle().addObserver(adapter);

        presenter = new SendFragmentPresenter(this, repository);
        presenter.loadSends(mEntityKey);

        return view;
    }

    public void show(List<Send> sends) {
        Resources resources = recyclerView.getContext().getResources();
        if (sends.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            recyclerView.setBackgroundColor(resources.getColor(R.color.cardview_light_background));
        } else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setBackgroundColor(resources.getColor(R.color.material_background));
        }
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
