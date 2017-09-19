package com.cragchat.mobile.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cragchat.mobile.R;
import com.cragchat.mobile.sql.UpdateRecentActivityTask;

public class RecentActivityFragment extends Fragment {


    public static RecentActivityFragment newInstance(int displayableId) {
        RecentActivityFragment f = new RecentActivityFragment();
        Bundle b = new Bundle();
        b.putString("id", "" + displayableId);
        f.setArguments(b);
        return f;
    }

    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recent_activity, container, false);
        id = Integer.parseInt(getArguments().getString("id"));

        ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar1);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.progressBox);
        layout.setVisibility(View.VISIBLE);

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.recycler_view);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
       // recList.addItemDecoration(new RecyclerViewMargin(48));

        new UpdateRecentActivityTask(getActivity(), id, recList, layout).execute();


        return view;
    }

    public class RecyclerViewMargin extends RecyclerView.ItemDecoration {
        private int margin;

        /**
         * constructor
         *
         * @param margin desirable margin size in px between the views in the recyclerView
         */
        public RecyclerViewMargin(@IntRange(from = 0) int margin) {
            this.margin = margin;

        }

        /**
         * Set different margins for the items inside the recyclerView: no top margin for the first row
         * and no left margin for the first column.
         */
        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            int position = parent.getChildLayoutPosition(view);
            //set right margin to all
            if (position > 0) {
                outRect.top = margin;
            }
        }
    }


}
