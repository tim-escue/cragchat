package com.cragchat.mobile.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.model.Image;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.List;


public class ProfileImagesFragment extends Fragment {

    public static ProfileImagesFragment newInstance(String username) {
        ProfileImagesFragment f = new ProfileImagesFragment();
        Bundle b = new Bundle();
        b.putString("username", username);
        f.setArguments(b);
        return f;
    }

    private List<Image> images;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_images, container, false);

        load(view);

        return view;
    }

    public void load(View v) {

        images = LocalDatabase.getInstance(getContext()).getImagesForProfile(getActivity(), getArguments().getString("username"));

        if (images != null && v != null) {
            // GridView gridview = (GridView) v.findViewById(R.id.thumbnail_grid);
            // gridview.setAdapter(new ImageAdapter(getContext(), images.toArray(new Image[images.size()])));
        }
    }

}
