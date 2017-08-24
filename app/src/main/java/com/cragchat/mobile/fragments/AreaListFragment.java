package com.cragchat.mobile.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.R;
import com.cragchat.mobile.adapters.AreaListAdapter;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AreaListFragment extends Fragment {

    private List<Area> allAreas;
    private NumRoutesSorter sorter;
    private NameSorter nameSorter;


    public static AreaListFragment newInstance(Area a) {
        AreaListFragment f = new AreaListFragment();
        Bundle b = new Bundle();
        b.putString("AREA", Displayable.encodeAsString(a));
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        sorter = new NumRoutesSorter(getContext());
        nameSorter = new NameSorter();
        Area thisArea = Displayable.decodeAreaString(getArguments().getString("AREA"));
        allAreas = LocalDatabase.getInstance(getActivity()).findAreasWithin(thisArea);

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_displayable_list, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.route_sort_spinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.area_sort_array, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(listener);


        ListView list = (ListView) view.findViewById(R.id.display_list);
        AreaListAdapter adap = new AreaListAdapter((CragChatActivity)getActivity(), allAreas);
        list.setAdapter(adap);
        list.setOnItemClickListener(adap);

        Collections .sort(allAreas);
        return view;
    }

    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.route_sort_spinner) {
                String option = parent.getItemAtPosition(position).toString();
                if (option.equals("Name")) {
                    Collections.sort(allAreas,nameSorter);
                } else if (option.equals("Num Routes")) {
                    Collections.sort(allAreas, sorter);
                }
                ListView lv = (ListView) getView().findViewById(R.id.display_list);
                BaseAdapter adap = (BaseAdapter) lv.getAdapter();
                adap.notifyDataSetChanged();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private class NumRoutesSorter implements Comparator<Area> {

        Context con;

        public NumRoutesSorter(Context con) {
            this.con = con;
        }

        @Override
        public int compare(Area one, Area two) {
            return LocalDatabase.getInstance(con).findRoutesWithin(two).size() - LocalDatabase.getInstance(con).findRoutesWithin(one).size();
        }


    };

    private class NameSorter implements Comparator<Area> {

        @Override
        public int compare(Area one, Area two) {
            return one.getName().compareTo(two.getName());
        }

    };

}
