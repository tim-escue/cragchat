package com.cragchat.mobile.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.SendListAdapter;
import com.cragchat.mobile.descriptor.Send;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.List;


public class SendsFragment extends Fragment {


    public static SendsFragment newInstance(int displayableId) {
        SendsFragment f = new SendsFragment();
        Bundle b = new Bundle();
        b.putString("id", ""+displayableId);
        f.setArguments(b);
        return f;
    }

    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_sends, container, false);
        id = Integer.parseInt(getArguments().getString("id"));

        List<Send> ratings = LocalDatabase.getInstance(getContext()).getSendsFor(id);

        final SendListAdapter adapter = new SendListAdapter(getActivity(), ratings, false);
        ListView lv = (ListView) view.findViewById(R.id.list_sends);

        if (ratings.size() == 0) {
            lv.setEmptyView(view.findViewById(R.id.list_empty));
        }

        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        return view;
    }


}
