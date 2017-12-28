package com.cragchat.mobile.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.cragchat.mobile.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by timde on 12/28/2017.
 */

public class MapActivity extends SearchableActivity implements OnMapReadyCallback {

    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, mMapFragment);
        fragmentTransaction.commit();
    }

    @Override
    int getToolbarColor() {
        return ContextCompat.getColor(this, R.color.primary);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
