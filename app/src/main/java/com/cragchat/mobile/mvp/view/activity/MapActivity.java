package com.cragchat.mobile.mvp.view.activity;

import android.os.Bundle;

import com.cragchat.mobile.R;
import com.cragchat.mobile.util.ViewUtil;
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
            ViewUtil.addFragmentToActivity(getFragmentManager(), MapFragment.newInstance(),
                    R.id.container, MAP_FRAG);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
