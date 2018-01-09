package com.cragchat.mobile.ui.presenter;

import android.arch.lifecycle.Lifecycle;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.model.Send;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;
import com.cragchat.mobile.ui.view.adapters.recycler.SendRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendFragmentPresenter {

    @BindView(R.id.list_sends)
    RecyclerView recyclerView;
    @BindView(R.id.list_empty)
    TextView empty;

    public SendFragmentPresenter(View parent, Lifecycle lifecycle, String entityKey) {
        ButterKnife.bind(this, parent);
        SendRecyclerAdapter adapter = SendRecyclerAdapter.create(entityKey);
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
        lifecycle.addObserver(adapter);
    }

    public void present(List<Send> sends) {
        Resources resources = recyclerView.getContext().getResources();
        if (sends.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            recyclerView.setBackgroundColor(resources.getColor(R.color.cardview_light_background));
        } else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setBackgroundColor(resources.getColor(R.color.material_background));
        }
    }
}