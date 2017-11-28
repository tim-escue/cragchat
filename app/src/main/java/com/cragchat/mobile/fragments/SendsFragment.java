package com.cragchat.mobile.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.SubmitSendActivity;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.network.Network;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.view.adapters.recycler.SendRecyclerAdapter;


public class SendsFragment extends Fragment implements View.OnClickListener {

    private String entityKey;

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

        if (Network.isConnected(getContext())) {
            Repository.getSends(entityKey, null);
        }

        final SendRecyclerAdapter adapter = SendRecyclerAdapter.create(entityKey);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.list_sends);
        RecyclerUtils.setAdapterAndManager(recList, adapter, LinearLayoutManager.VERTICAL);

        getLifecycle().addObserver(adapter);

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {

            if (Authentication.isLoggedIn(getContext())) {
                Intent next = new Intent(getContext(), SubmitSendActivity.class);
                next.putExtra("entityKey", entityKey);
                startActivity(next);
            } else {
                Toast.makeText(getContext(), "Must be logged in to submit send", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
