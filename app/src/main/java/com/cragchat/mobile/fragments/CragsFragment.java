package com.cragchat.mobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.model.LegacyArea;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.view.adapters.recycler.CragsFragmentRecyclerAdapter;

/**
 * Created by timde on 9/9/2017.
 */

public class CragsFragment extends Fragment {

    public static CragsFragment newInstance() {
        return new CragsFragment();
    }

    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_crags, container, false);

       // ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar1);
       // RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.progressBox);
       // layout.setVisibility(View.VISIBLE);

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.crags_recycler);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        CragsFragmentRecyclerAdapter adapter = new CragsFragmentRecyclerAdapter((CragChatActivity)getActivity());
        LegacyArea ozone = (LegacyArea)LocalDatabase.getInstance(getContext()).findExact("Ozone");
//        Log.d("SO weird", "ozone is:" + ozone.toString());
        if (ozone != null) {
            adapter.addItem(ozone);
        }

        recList.setAdapter(adapter);

        return view;
    }
}
