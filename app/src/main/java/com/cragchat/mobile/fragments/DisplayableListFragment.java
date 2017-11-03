package com.cragchat.mobile.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.model.Displayable;
import com.cragchat.mobile.model.LegacyArea;
import com.cragchat.mobile.model.LegacyRoute;
import com.cragchat.mobile.view.adapters.recycler.DisplayableRecyclerAdapter;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;

import java.util.Comparator;
import java.util.List;

import static android.view.View.GONE;


public class DisplayableListFragment extends Fragment {

    private List<Object> allRoutes;
    private YdsSorter sorter;
    private TypeSorter typeSorter;
    private View filterView;
    private DisplayableRecyclerAdapter adap;
    private PopupWindow popupWindow;
    private boolean hideFilter;

    public static DisplayableListFragment newInstance() {
        DisplayableListFragment f = new DisplayableListFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    public void setDisplayables(List list) {
        allRoutes = list;
        if (adap != null) {
            adap.notifyDataSetChanged();
        }
    }

    public void hideFilterAndSortButtons() {
        hideFilter = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_displayable_list, container, false);

        sorter = new YdsSorter(getContext());
        typeSorter = new TypeSorter();


        Spinner spinner = (Spinner) view.findViewById(R.id.route_sort_spinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.route_sort_array, R.layout.spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(listener);

        adap = new DisplayableRecyclerAdapter((CragChatActivity) getActivity(), allRoutes);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.comment_section_list);
        RecyclerUtils.setAdapterAndManager(recList, adap, LinearLayoutManager.VERTICAL);


        if (!hideFilter) {
            filterView = getLayoutInflater().inflate(R.layout.fragment_filter, null);
            LinearLayout filterButton = view.findViewById(R.id.filter_button);
            SwitchCompat switchButton = filterView.findViewById(R.id.sport);
            switchButton.setOnCheckedChangeListener(filterListener);
            switchButton = filterView.findViewById(R.id.trad);
            switchButton.setOnCheckedChangeListener(filterListener);
            switchButton = filterView.findViewById(R.id.toprope);
            switchButton.setOnCheckedChangeListener(filterListener);

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
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.showAsDropDown(view);
                    }
                }
            });
        } else {
            RelativeLayout sortAndFilterOptions = view.findViewById(R.id.sort_filter_options);
            sortAndFilterOptions.setVisibility(GONE);
        }

        //  Collections.sort(allRoutes);
        return view;
    }

    private CompoundButton.OnCheckedChangeListener filterListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            String filter = compoundButton.getText().toString();
            if (b) {
                adap.removeFilter(filter);
            } else {
                adap.addFilter(filter);
            }
        }
    };

    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.route_sort_spinner) {
                String option = parent.getItemAtPosition(position).toString();
                if (option.equals("NAME")) {
                    //  Collections.sort(allRoutes);
                } else if (option.equals("YDS")) { //Used to be YDS: Low -> High
                    sorter.setHigh(false);
                    //  Collections.sort(allRoutes, sorter);
                } else if (option.equals("TYPE")) {
                    //  Collections.sort(allRoutes, typeSorter);
                }
                adap.notifyDataSetChanged();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private class YdsSorter implements Comparator<Displayable> {

        private boolean high = false;
        private Context con;

        public YdsSorter(Context con) {
            this.con = con;
        }

        public void setHigh(boolean high) {
            this.high = high;
        }

        @Override
        public int compare(Displayable one, Displayable two) {
            if (one instanceof LegacyArea && two instanceof LegacyRoute) {
                return -1;
            } else if (two instanceof LegacyArea && one instanceof LegacyRoute) {
                return 1;
            } else if (one instanceof LegacyArea && two instanceof LegacyArea) {
                return 0;
            } else {
                LegacyRoute routeOne = (LegacyRoute) one;
                LegacyRoute routeTwo = (LegacyRoute) two;
                if (high) {
                    return routeTwo.getYds(con) - routeOne.getYds(con);
                } else {
                    return routeOne.getYds(con) - routeTwo.getYds(con);
                }
            }
        }


    }

    ;

    private class TypeSorter implements Comparator<Displayable> {

        @Override
        public int compare(Displayable one, Displayable two) {
            if (one instanceof LegacyArea && two instanceof LegacyRoute) {
                return -1;
            } else if (two instanceof LegacyArea && one instanceof LegacyRoute) {
                return 1;
            } else if (one instanceof LegacyArea && two instanceof LegacyArea) {
                return -1;
            } else {
                LegacyRoute routeOne = (LegacyRoute) one;
                LegacyRoute routeTwo = (LegacyRoute) two;
                return routeOne.getType().compareTo(routeTwo.getType());
            }
        }

    }

    ;


}
