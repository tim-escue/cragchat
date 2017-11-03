package com.cragchat.mobile.fragments;


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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.database.models.RealmRoute;
import com.cragchat.mobile.util.JsonUtil;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.view.adapters.recycler.RouteListRecyclerAdapter;
import com.cragchat.networkapi.ErrorHandlingObserverable;
import com.cragchat.networkapi.NetworkApi;

import java.util.List;

import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmQuery;


public class RouteListFragment extends Fragment {

    private static final String IDS_TAG = "routeIds";
    private static final String AREA_TAG = "areaKey";

    private String[] routeIds;
    private String areaKey;

    private View filterView;
    private RouteListRecyclerAdapter adap;
    private PopupWindow popupWindow;
    private Realm mRealm;
    boolean filterSport;
    boolean filterTrad;
    boolean filterMixed;

    private SwitchCompat sportSwitch;
    private SwitchCompat tradSwitch;
    private SwitchCompat mixedSwitch;

    public static RouteListFragment newInstance(String areaKey, String[] routeIds) {
        RouteListFragment f = new RouteListFragment();
        Bundle b = new Bundle();
        b.putStringArray(IDS_TAG, routeIds);
        b.putString(AREA_TAG, areaKey);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_displayable_list, container, false);

        routeIds = getArguments().getStringArray(IDS_TAG);

        if (NetworkApi.isConnected(getContext())) {
            updateRoutes(routeIds);
        }

        mRealm = Realm.getDefaultInstance();

        Spinner spinner = (Spinner) view.findViewById(R.id.route_sort_spinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.route_sort_array, R.layout.spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] sortOptions = getContext().getResources().getStringArray(R.array.route_sort_array);
                System.out.println("SORTINGby:" + sortOptions[i]);
                if (sortOptions[i].equals("Yds")) {
                    adap.sort(RealmRoute.FIELD_YDS);
                } else if (sortOptions[i].equals("Type")) {
                    adap.sort(RealmRoute.FIELD_TYPE);
                } else {
                    adap.sort(RealmRoute.FIELD_NAME);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adap = new RouteListRecyclerAdapter(
                mRealm.where(RealmRoute.class).in(RealmRoute.FIELD_KEY, routeIds).findAll(),
                true,
                (CragChatActivity) getActivity());
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.comment_section_list);
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

    private void updateRoutes(String[] routeIds) {
        String json = JsonUtil.stringArrayToJSon(routeIds);
        NetworkApi.getInstance().getRoutes(json)
                .subscribeOn(Schedulers.io())
                .subscribe(new ErrorHandlingObserverable<List<RealmRoute>>() {
                    @Override
                    public void onSuccess(final List<RealmRoute> realmRoutes) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.insertOrUpdate(realmRoutes);
                            }
                        });
                        realm.close();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private CompoundButton.OnCheckedChangeListener filterListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            RealmQuery<RealmRoute> routeQuery = mRealm.where(RealmRoute.class)
                    .in(RealmRoute.FIELD_KEY, routeIds);
            if (!sportSwitch.isChecked()) {
                routeQuery.notEqualTo(RealmRoute.FIELD_TYPE, "Sport");
            }
            if (!tradSwitch.isChecked()) {
                routeQuery.notEqualTo(RealmRoute.FIELD_TYPE, "Trad");
            }
            if (!mixedSwitch.isChecked()) {
                routeQuery.notEqualTo(RealmRoute.FIELD_TYPE, "Sport,Trad");
            }
            adap.updateData(routeQuery.findAll());
        }
    };

}
