package com.cragchat.mobile.mvp.view.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.cragchat.mobile.R;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.data.local.realm.RealmRoute;
import com.cragchat.mobile.mvp.view.activity.CragChatActivity;
import com.cragchat.mobile.mvp.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.mvp.view.adapters.recycler.RouteListRecyclerAdapter;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;
import io.realm.Sort;


public class
RouteListFragment extends DaggerFragment {

    private View filterView;
    private RouteListRecyclerAdapter adap;
    private PopupWindow popupWindow;
    private SwitchCompat sportSwitch;
    private SwitchCompat tradSwitch;
    private SwitchCompat mixedSwitch;

    private CompoundButton.OnCheckedChangeListener filterListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            adap.filter(sportSwitch.isChecked(), tradSwitch.isChecked(), mixedSwitch.isChecked());
        }
    };

    @Inject
    Repository repository;

    @Inject
    @Named(InjectionNames.ROUTE_IDS)
    String[] routeIds;

    @Inject
    public RouteListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_displayable_list, container, false);

        //Called to update local from remote for realm adapter to use.
        repository.observeRoutes(routeIds).subscribe();

        Spinner spinner = (Spinner) view.findViewById(R.id.route_sort_spinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.route_sort_array, R.layout.spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] sortOptions = getContext().getResources().getStringArray(R.array.route_sort_array);
                if (sortOptions[i].equals("Yds")) {
                    adap.sort(RealmRoute.FIELD_YDS, Sort.DESCENDING);
                } else if (sortOptions[i].equals("Type")) {
                    adap.sort(RealmRoute.FIELD_TYPE, Sort. ASCENDING);
                } else {
                    adap.sort(RealmRoute.FIELD_NAME, Sort.ASCENDING);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adap = RouteListRecyclerAdapter.create(routeIds, (CragChatActivity) getActivity());
        getLifecycle().addObserver(adap);
        RecyclerView recList = view.findViewById(R.id.comment_section_list);
        RecyclerUtils.setAdapterAndManager(recList, adap, LinearLayoutManager.VERTICAL);

        filterView = getLayoutInflater().inflate(R.layout.fragment_filter, null);
        RelativeLayout filterButton = view.findViewById(R.id.filter_button);
        sportSwitch = filterView.findViewById(R.id.sport);
        sportSwitch.setOnCheckedChangeListener(filterListener);
        tradSwitch = filterView.findViewById(R.id.trad);
        tradSwitch.setOnCheckedChangeListener(filterListener);
        mixedSwitch = filterView.findViewById(R.id.toprope);
        mixedSwitch.setOnCheckedChangeListener(filterListener);

        Button doneButton = filterView.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow == null || !popupWindow.isShowing()) {
                    popupWindow = new PopupWindow(
                            filterView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));
                    popupWindow.setElevation(16);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.showAsDropDown(view);
                }
            }
        });


        return view;
    }

}
