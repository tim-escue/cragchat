package chat.crag.cragchat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;
import chat.crag.cragchat.R;
import chat.crag.cragchat.adapters.RatingListAdapter;
import chat.crag.cragchat.descriptor.Rating;
import chat.crag.cragchat.sql.LocalDatabase;
import chat.crag.cragchat.sql.SendPrivacyChangeTask;
import chat.crag.cragchat.user.User;

import java.util.List;


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
