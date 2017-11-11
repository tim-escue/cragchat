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
import com.cragchat.mobile.model.realm.RealmArea;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.view.adapters.recycler.CragsFragmentRecyclerAdapter;

import io.realm.Realm;

/**
 * Created by timde on 9/9/2017.
 */

public class CragsFragment extends Fragment {

    public static CragsFragment newInstance() {
        return new CragsFragment();
    }

    private Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_crags, container, false);

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.crags_recycler);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        realm = Realm.getDefaultInstance();

        CragsFragmentRecyclerAdapter adapter = new CragsFragmentRecyclerAdapter(
                realm.where(RealmArea.class).equalTo(RealmArea.FIELD_NAME, "Ozone").findAll(),
                true,
                (CragChatActivity) getActivity());
        recList.setAdapter(adapter);

        /*
            RecyclerView uses a RealmAdapter which depends on realm-specific api so Repository
            which uses abstract interfaces will not provide right type. We still call repository
            so that local database is updated from network. RealmAdapter will auto-update
            when the local database (Realm) is updated.
         */
        Repository.getAreaByName("Ozone", getContext());


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
