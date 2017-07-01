package chat.crag.cragchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import chat.crag.cragchat.R;
import chat.crag.cragchat.adapters.LocationPagerAdapter;
import chat.crag.cragchat.android.SlidingTabLayout;

public class LocationFragment extends Fragment {

    public static LocationFragment newInstance(int routeId) {
        LocationFragment f = new LocationFragment();
        Bundle b = new Bundle();
        b.putInt("routeId", routeId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int routeId = getArguments().getInt("routeId");
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        LocationPagerAdapter pageAdapter = new LocationPagerAdapter(getChildFragmentManager(), routeId);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager_inside);
        pager.setAdapter(pageAdapter);
        pager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(
                        event.getAction() == MotionEvent.ACTION_DOWN &&
                                v instanceof ViewGroup
                        ) {
                    ((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        SlidingTabLayout slab = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs_inside);
        slab.setViewPager(pager);

        return view;
    }
}