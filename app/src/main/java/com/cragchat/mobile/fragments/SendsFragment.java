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
import com.cragchat.mobile.adapters.recycler.SendRecyclerAdapter;
import com.cragchat.mobile.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.descriptor.Send;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.user.User;

import java.util.List;


public class SendsFragment extends Fragment implements View.OnClickListener {


    public static SendsFragment newInstance(int displayableId) {
        SendsFragment f = new SendsFragment();
        Bundle b = new Bundle();
        b.putString("id", "" + displayableId);
        f.setArguments(b);
        return f;
    }

    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_sends, container, false);
        id = Integer.parseInt(getArguments().getString("id"));

        List<Send> ratings = LocalDatabase.getInstance(getContext()).getSendsFor(id);

        final SendRecyclerAdapter adapter = new SendRecyclerAdapter(getActivity(), ratings, false);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.list_sends);
        RecyclerUtils.setAdapterAndManager(recList, adapter, LinearLayoutManager.VERTICAL);
      //  adapter.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {

            if (User.currentToken(getActivity()) != null) {
                Intent next = new Intent(getContext(), SubmitSendActivity.class);
                next.putExtra("id", id);
                startActivity(next);
            } else {
                Toast.makeText(getContext(), "Must be logged in to submit send", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
