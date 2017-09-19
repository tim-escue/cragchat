package com.cragchat.mobile.adapters.recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by timde on 9/14/2017.
 */

public class RecyclerUtils {

    public static void setAdapterAndManager(RecyclerView view,
                                            RecyclerView.Adapter adapter,
                                            int orientation) {
        view.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(orientation);
        view.setLayoutManager(llm);
        view.setAdapter(adapter);
    }
}
