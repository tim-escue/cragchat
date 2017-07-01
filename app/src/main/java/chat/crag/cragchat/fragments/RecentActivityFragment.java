package chat.crag.cragchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import chat.crag.cragchat.R;
import chat.crag.cragchat.sql.UpdateRecentActivityTask;

public class RecentActivityFragment extends Fragment {


    public static RecentActivityFragment newInstance(int displayableId) {
        RecentActivityFragment f = new RecentActivityFragment();
        Bundle b = new Bundle();
        b.putString("id", ""+displayableId);
        f.setArguments(b);
        return f;
    }

    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recent_activity, container, false);
        id = Integer.parseInt(getArguments().getString("id"));

        ProgressBar bar = (ProgressBar)view.findViewById(R.id.progressBar1);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.progressBox);
        layout.setVisibility(View.VISIBLE);

        new UpdateRecentActivityTask(getActivity(), id, (ListView) view.findViewById(R.id.list_recent_activity), layout).execute();



        return view;
    }



}
