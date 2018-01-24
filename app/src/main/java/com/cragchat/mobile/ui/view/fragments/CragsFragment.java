package com.cragchat.mobile.ui.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.view.adapters.recycler.CragsFragmentRecyclerAdapter;

/**
 * Created by timde on 9/9/2017.
 */

public class CragsFragment extends BaseFragment {

    public static CragsFragment newInstance() {
        return new CragsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_crags, container, false);

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.crags_recycler);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        CragsFragmentRecyclerAdapter adapter = CragsFragmentRecyclerAdapter.create("Ozone", getActivity());
        recList.setAdapter(adapter);
        getLifecycle().addObserver(adapter);

        /*
            RecyclerView uses a RealmAdapter which depends on realm-specific api so Repository
            which uses abstract interfaces will not provide right type. We still call repository
            so that local database is updated from network. RealmAdapter will auto-update
            when the local database (Realm) is updated.
         */
        repository.getAreaByName("Ozone", null);


        return view;
    }


}
