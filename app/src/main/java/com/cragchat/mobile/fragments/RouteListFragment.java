package com.cragchat.mobile.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.adapters.RouteListAdapter;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class RouteListFragment extends Fragment {

    private List<Route> allRoutes;
    private YdsSorter sorter;
    private TypeSorter typeSorter;


    public static RouteListFragment newInstance(Area a) {
        RouteListFragment f = new RouteListFragment();
        Bundle b = new Bundle();
        b.putString("AREA", Displayable.encodeAsString(a));
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_displayable_list, container, false);

        sorter = new YdsSorter(getContext());
        typeSorter = new TypeSorter();
        Area thisArea = Displayable.decodeAreaString(getArguments().getString("AREA"));
        allRoutes = LocalDatabase.getInstance(getActivity()).findRoutesWithin(thisArea);


       /* Spinner spinner = (Spinner) view.findViewById(R.id.route_sort_spinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.route_sort_array, R.layout.spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(listener);*/

        ListView list = (ListView) view.findViewById(R.id.display_list);
        RouteListAdapter adap = new RouteListAdapter((CragChatActivity) getActivity(), allRoutes);
        list.setAdapter(adap);
        list.setOnItemClickListener(adap);

        Collections.sort(allRoutes);
        return view;
    }


    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            /*if (parent.getId() == R.id.route_sort_spinner) {
                String option = parent.getItemAtPosition(position).toString();
                if (option.equals("Name")) {
                    Collections.sort(allRoutes);
                } else if (option.equals("YDS: High -> Low")) {
                    sorter.setHigh(true);
                    Collections.sort(allRoutes, sorter);
                } else if (option.equals("YDS: Low -> High")) {
                    sorter.setHigh(false);
                    Collections.sort(allRoutes, sorter);
                } else if (option.equals("Type")) {
                    Collections.sort(allRoutes, typeSorter);
                }
                ListView lv = (ListView) getView().findViewById(R.id.display_list);
                BaseAdapter adap = (BaseAdapter) lv.getAdapter();
                adap.notifyDataSetChanged();
            }
*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private class YdsSorter implements Comparator<Route> {

        private boolean high = false;
        private Context con;

        public YdsSorter(Context con) {
            this.con = con;
        }

        public void setHigh(boolean high) {
            this.high = high;
        }

        @Override
        public int compare(Route one, Route two) {
            if (high) {
                return two.getYds(con) - one.getYds(con);
            } else {
                return one.getYds(con) - two.getYds(con);
            }
        }


    }

    ;

    private class TypeSorter implements Comparator<Route> {

        @Override
        public int compare(Route one, Route two) {
            return one.getType().compareTo(two.getType());
        }

    }

    ;


}
