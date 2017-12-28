package com.cragchat.mobile.fragments;


import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.SubmitSendActivity;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.model.Send;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.view.adapters.recycler.SendRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SendsFragment extends Fragment implements View.OnClickListener {

    private String entityKey;
    private SendFragmentPresenter presenter;

    public static SendsFragment newInstance(String displayableId) {
        SendsFragment f = new SendsFragment();
        Bundle b = new Bundle();
        b.putString("entityKey", displayableId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_sends, container, false);
        entityKey = getArguments().getString("entityKey");

        /*
            Called only to update sends. The return value is not used because SendRecyclerAdapter
            relies upon an instance of Realm and Repository can not expose Realm API in order to
            preserve CLEAN architecture.
         */
        presenter = new SendFragmentPresenter(view, getLifecycle());
        List<Send> sends = Repository.getSends(entityKey, new Callback<List<Send>>() {
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

    class SendFragmentPresenter {

        @BindView(R.id.list_sends)
        RecyclerView recyclerView;
        @BindView(R.id.list_empty)
        TextView empty;
        private SendRecyclerAdapter adapter;

        public SendFragmentPresenter(View parent, Lifecycle lifecycle) {
            ButterKnife.bind(this, parent);
            adapter = SendRecyclerAdapter.create(entityKey);
            RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
            lifecycle.addObserver(adapter);
        }

        public void present(List<Send> sends) {
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
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (Authentication.isLoggedIn(getContext())) {
                Intent next = new Intent(getContext(), SubmitSendActivity.class);
                next.putExtra("entityKey", entityKey);
                startActivity(next);
            } else {
                DialogFragment df = NotificationDialog.newInstance("Must be logged in to add a send.");
                df.show(getFragmentManager(), "dialog");
            }
        }
    }
}
