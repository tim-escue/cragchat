package com.cragchat.mobile.ui.view.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.cragchat.mobile.R;
import com.cragchat.mobile.util.ActivityUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by timde on 12/28/2017.
 */

public class MapActivity extends SearchableActivity implements OnMapReadyCallback {

    private static final String MAP_FRAG = "map_frag";

    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (getSupportFragmentManager().findFragmentByTag(MAP_FRAG) == null) {
            ActivityUtil.addFragmentToActivity(getFragmentManager(), MapFragment.newInstance(),
                    R.id.container, MAP_FRAG);
        }
    }

    @Override
    public int getToolbarColor() {
        return ContextCompat.getColor(this, R.color.primary);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
