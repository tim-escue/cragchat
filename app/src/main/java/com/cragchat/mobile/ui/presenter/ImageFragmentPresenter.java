package com.cragchat.mobile.ui.presenter;

import android.arch.lifecycle.Lifecycle;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.model.Image;
import com.cragchat.mobile.ui.view.adapters.recycler.ImageRecyclerAdapter;
import com.cragchat.mobile.ui.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageFragmentPresenter {

    @BindView(R.id.images_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.list_empty)
    TextView empty;
    private ImageRecyclerAdapter adapter;

    public ImageFragmentPresenter(View parent, Lifecycle lifecycle, String entityKey) {
        ButterKnife.bind(this, parent);
        adapter = ImageRecyclerAdapter.create(entityKey, parent.getContext());
        RecyclerUtils.setAdapterAndManager(recyclerView, adapter, LinearLayoutManager.VERTICAL);
        lifecycle.addObserver(adapter);
    }

    public void present(List<Image> images) {
        Resources resources = recyclerView.getContext().getResources();
        if (images.isEmpty()) {
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