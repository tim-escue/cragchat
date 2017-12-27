package com.cragchat.mobile.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.cragchat.mobile.model.realm.RealmArea;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.view.adapters.recycler.AreaListRecyclerAdapter;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;

import static android.view.View.GONE;


public class AreaListFragment extends Fragment {

    private static final String IDS_String = "routeIds";
    private static final String AREA_String = "areaKey";

    private String[] areaIds;
    private String areaKey;

    private AreaListRecyclerAdapter adap;

    public static AreaListFragment newInstance(String areaKey, String[] areaIds) {
        AreaListFragment f = new AreaListFragment();
        Bundle b = new Bundle();
        b.putStringArray(IDS_String, areaIds);
        b.putString(AREA_String, areaKey);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_displayable_list, container, false);

        areaIds = getArguments().getStringArray(IDS_String);

        Repository.getAreas(areaIds, null);

        Spinner spinner = (Spinner) view.findViewById(R.id.route_sort_spinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.area_sort_array, R.layout.spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        View filterView = view.findViewById(R.id.filter_button);
        filterView.setVisibility(GONE);

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

        adap = AreaListRecyclerAdapter.create(getActivity(), areaIds);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.comment_section_list);
        recList.setItemAnimator(new DefaultItemAnimator());
        RecyclerUtils.setAdapterAndManager(recList, adap, LinearLayoutManager.VERTICAL);

        getLifecycle().addObserver(adap);

        return view;
    }


}
