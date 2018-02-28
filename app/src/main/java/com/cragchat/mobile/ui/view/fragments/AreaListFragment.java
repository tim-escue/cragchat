package com.cragchat.mobile.ui.view.fragments;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cragchat.mobile.R;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.realm.RealmArea;
import com.cragchat.mobile.ui.view.adapters.recycler.AreaListRecyclerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;


public class AreaListFragment extends DaggerFragment {

    @Inject
    Repository repository;

    @Inject
    @Named(InjectionNames.AREA_IDS)
    String[] areaIds;

    @Inject
    public AreaListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_displayable_list, container, false);


        repository.observeAreas(areaIds).subscribe();

        Spinner spinner = (Spinner) view.findViewById(R.id.route_sort_spinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.area_sort_array, R.layout.spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        View filterView = view.findViewById(R.id.filter_button);
        filterView.setVisibility(GONE);

        final AreaListRecyclerAdapter adap = AreaListRecyclerAdapter.create(getActivity(), areaIds);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] sortOptions = getContext().getResources().getStringArray(R.array.area_sort_array);
                if (sortOptions[i].equals("Name")) {
                    adap.sort(RealmArea.FIELD_NAME);
                } else {
                    adap.sort(RealmArea.FIELD_ROUTES);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.comment_section_list);
        recList.setItemAnimator(new DefaultItemAnimator());
        RecyclerUtils.setAdapterAndManager(recList, adap, LinearLayoutManager.VERTICAL);

        getLifecycle().addObserver(adap);

        return view;
    }


}
