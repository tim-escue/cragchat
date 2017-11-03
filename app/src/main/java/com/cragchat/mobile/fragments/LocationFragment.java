package com.cragchat.mobile.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.view.adapters.pager.LocationPagerAdapter;
import com.cragchat.mobile.view.adapters.pager.TabPagerAdapter;

public class LocationFragment extends Fragment implements View.OnClickListener {

    private TabPagerAdapter pageAdapter;
    private ViewPager pager;

    public static LocationFragment newInstance(String routeId) {
        LocationFragment f = new LocationFragment();
        Bundle b = new Bundle();
        b.putString("routeId", routeId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = pageAdapter.getItem(pager.getCurrentItem());
        if (fragment instanceof View.OnClickListener) {
            View.OnClickListener clickListener = (View.OnClickListener) fragment;
            clickListener.onClick(view);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String routeId = getArguments().getString("routeId");
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        pageAdapter = new LocationPagerAdapter(getChildFragmentManager(), routeId, (FloatingActionButton) getActivity().findViewById(R.id.add_button));
        pager = (ViewPager) view.findViewById(R.id.pager_inside);
        pager.setAdapter(pageAdapter);
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v instanceof ViewGroup) {
                    ((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        TabLayout slab = (TabLayout) view.findViewById(R.id.sliding_tabs_inside);
        slab.setupWithViewPager(pager);

        return view;
    }
}