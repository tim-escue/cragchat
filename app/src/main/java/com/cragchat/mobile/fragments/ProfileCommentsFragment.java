package com.cragchat.mobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.cragchat.mobile.R;
import com.cragchat.mobile.view.adapters.ProfileCommentsListAdapter;
import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.comments.ProfileCommentManager;
import com.cragchat.mobile.sql.LocalDatabase;

public class ProfileCommentsFragment extends Fragment {

    private ProfileCommentManager manager;

    public static ProfileCommentsFragment newInstance(String username) {
        ProfileCommentsFragment f = new ProfileCommentsFragment();
        Bundle b = new Bundle();
        b.putString("username", username);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile_comments, container, false);

        manager = new ProfileCommentManager();

        for (Comment i : LocalDatabase.getInstance(getContext()).getCommentsForProfile(getActivity(), getArguments().getString("username"))) {
            manager.addComment(i);
        }
        manager.sortByScore();
        //System.out.println("MANAGER SIZE:" + manager.getCommentList().size() + "\n");


        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_comment_sort);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_comment_sort_options, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(listener);

        final ProfileCommentsListAdapter adapter = new ProfileCommentsListAdapter(getActivity(), manager);
        ListView lv = (ListView) view.findViewById(R.id.list_profile_comments);
        if (manager.getCommentList().size() == 0) {
            lv.setEmptyView(view.findViewById(R.id.list_empty));
        }
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.spinner_comment_sort) {
                String option = parent.getItemAtPosition(position).toString();
                if (option.equals("Score")) {
                    manager.sortByScore();
                } else if (option.equals("Date")) {
                    manager.sortByDate();
                }
                ListView lv = (ListView) getView().findViewById(R.id.list_profile_comments);
                BaseAdapter adap = (BaseAdapter) lv.getAdapter();
                adap.notifyDataSetChanged();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
