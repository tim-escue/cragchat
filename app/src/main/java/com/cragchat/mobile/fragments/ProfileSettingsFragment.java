package com.cragchat.mobile.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import com.cragchat.mobile.R;


public class ProfileSettingsFragment extends Fragment {


    public static ProfileSettingsFragment newInstance() {
        ProfileSettingsFragment f = new ProfileSettingsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        ToggleButton button = (ToggleButton) view.findViewById(R.id.toggle_private);
        //button.setChecked(User.isPrivate(getActivity()));

        //button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
         //       new SendPrivacyChangeTask(getActivity(), isChecked).execute();
         //   }
        //});
        return view;
    }


}
