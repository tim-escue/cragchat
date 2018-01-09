package com.cragchat.mobile.ui.view.adapters.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public static View getItemView(ViewGroup parent, @LayoutRes int layout) {
        return LayoutInflater.
                from(parent.getContext()).
                inflate(layout, parent, false);
    }
}
