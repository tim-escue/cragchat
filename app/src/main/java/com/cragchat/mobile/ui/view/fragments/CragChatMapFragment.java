package com.cragchat.mobile.ui.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CragChatMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static View view;
    LocationManager lm;
    private GoogleApiClient apiClient;
    private Location curLoc;
    private GoogleMap map;

    public static CragChatMapFragment newInstance() {
        CragChatMapFragment f = new CragChatMapFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_map, container, false);
        /*
            apiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


            startLocationUpdates();*/
        ViewGroup mapLayout = view.findViewById(R.id.map_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapLayout.requestTransparentRegion(mapLayout);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        //  stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        //startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setCostAllowed(false);
            lm.requestLocationUpdates(lm.getBestProvider(criteria, true), 3000, 0, this);
        }
    }

    private void stopLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*map = googleMap;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            //Log.d("NO MAPLOC");
        }*/
        LatLng ozone = new LatLng(45.567279, -122.211022);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ozone, 10));
        googleMap.addMarker(new MarkerOptions()
                .position(ozone)
                .title("Ozone"));
        ///apiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            curLoc = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            if (curLoc != null) {
                //Log.d(curLoc.getLatitude() + " " + curLoc.getLongitude());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.d("LOCATION", "SUSPENDED");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Log.d("LOCATION", "FAILED LOCATION");
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.d("LOCATION", "CHANGED: " + location.getAccuracy() + " " + location.toString());

        if (map != null && isBetterLocation(location, curLoc)) {
            map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            curLoc = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        //Log.d("LOCATION", "PROVIDER ENABLED");

    }

    @Override
    public void onProviderDisabled(String provider) {
        //Log.d("LOCATION", "PROVIDER DISABLED");

    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}
