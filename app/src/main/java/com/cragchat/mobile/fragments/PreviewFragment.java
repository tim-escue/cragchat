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

import com.cragchat.mobile.R;
import com.cragchat.mobile.sql.UpdateRecentActivityTask;

public class PreviewFragment extends Fragment {

    public static PreviewFragment newInstance(int displayableId) {
        PreviewFragment f = new PreviewFragment();
        Bundle b = new Bundle();
        b.putString("id", "" + displayableId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_preview, container, true);

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.recycler_view);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.addItemDecoration(new RecyclerViewMargin(48));

        ProgressBar layout = (ProgressBar) view.findViewById(R.id.progressBar1);
        new UpdateRecentActivityTask(getActivity(), -1, recList, layout).execute();


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