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
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.view.adapters.recycler.CragsFragmentRecyclerAdapter;
import com.cragchat.networkapi.NetworkApi;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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


        if (NetworkApi.isConnected(getContext())) {
            NetworkApi.getInstance().getArea(null, "Ozone").subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<RealmArea>() {
                        @Override
                        public void accept(final RealmArea area) throws Exception {
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.insertOrUpdate(area);
                                }
                            });
                            realm.close();
                        }
                    });
        }


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
