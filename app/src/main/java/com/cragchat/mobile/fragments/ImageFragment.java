package com.cragchat.mobile.fragments;


import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.EditImageActivity;
import com.cragchat.mobile.activity.RouteActivity;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.model.Image;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.view.adapters.recycler.ImageRecyclerAdapter;
import com.cragchat.mobile.view.adapters.recycler.RecyclerUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageFragment extends Fragment implements View.OnClickListener {

    public static final int PICK_IMAGE = 873;
    private String key;
    private ImageFragmentPresenter presenter;

    public static ImageFragment newInstance(String displayableId) {
        ImageFragment f = new ImageFragment();
        Bundle b = new Bundle();
        b.putString("id", displayableId);
        f.setArguments(b);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        key = getArguments().getString("id");

        /*
            Called only to update images. The return value is not used because ImageRecyclerAdapte
            relies upon an instance of Realm and Repository can not expose Realm API in order to
            preserve CLEAN architecture.
         */
        presenter = new ImageFragmentPresenter(view, getLifecycle());
        List<Image> sends = Repository.getImages(key, new Callback<List<Image>>() {
            @Override
            public void onSuccess(List<Image> object) {
                presenter.present(object);
            }

            @Override
            public void onFailure() {

            }
        });
        presenter.present(sends);

        return view;
    }

    class ImageFragmentPresenter {

        @BindView(R.id.images_recycler)
        RecyclerView recyclerView;
        @BindView(R.id.list_empty)
        TextView empty;
        private ImageRecyclerAdapter adapter;

        public ImageFragmentPresenter(View parent, Lifecycle lifecycle) {
            ButterKnife.bind(this, parent);
            adapter = ImageRecyclerAdapter.create(key, parent.getContext());
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            if (Authentication.isLoggedIn(getContext())) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            } else {
                DialogFragment df = NotificationDialog.newInstance("Must be logged in to add an image.");
                df.show(getFragmentManager(), "dialog");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                Intent editImage = new Intent(getContext(), EditImageActivity.class);
                editImage.putExtra("image_uri", data.getData().toString());
                editImage.putExtra(EditImageActivity.ENTITY_KEY, key);
                Activity activity = getActivity();
                editImage.putExtra(EditImageActivity.ENTITY_TYPE, activity instanceof RouteActivity
                        ? EditImageActivity.TYPE_ROUTE : EditImageActivity.TYPE_AREA);
                startActivity(editImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}