package chat.crag.cragchat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import chat.crag.cragchat.R;
import chat.crag.cragchat.adapters.RatingListAdapter;
import chat.crag.cragchat.descriptor.Rating;
import chat.crag.cragchat.sql.LocalDatabase;

import java.util.List;


public class RatingFragment extends Fragment {


    public static RatingFragment newInstance(int displayableId) {
        RatingFragment f = new RatingFragment();
        Bundle b = new Bundle();
        b.putString("id", ""+displayableId);
        f.setArguments(b);
        return f;
    }

    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_ratings, container, false);
        id = Integer.parseInt(getArguments().getString("id"));

        List<Rating> ratings = LocalDatabase.getInstance(getContext()).getRatingsFor(id);

        final RatingListAdapter adapter = new RatingListAdapter(getActivity(), ratings, false);
        ListView lv = (ListView) view.findViewById(R.id.list_ratings);

        if (ratings.size() == 0) {
            lv.setEmptyView(view.findViewById(R.id.list_empty));
        }

        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }


}
