package com.cragchat.mobile.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
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
    @BindView(R.id.list_empty)
    TextView empty;
    @BindView(R.id.images_recycler)
    RecyclerView recyclerView;
    private String key;
    private ImageRecyclerAdapter adap;

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

        ButterKnife.bind(this, view);

        key = getArguments().getString("id");

        load();

        /*
            Called with a null callback because the adapter consumes Realm API and handles
            updating data internally. Method is called simply to update local data from remote.
         */
        if (Repository.getImages(key, new Callback<List<Image>>() {
            @Override
            public void onSuccess(List<Image> object) {
                hideEmptyIfShowing();
            }

            @Override
            public void onFailure() {

            }
        }) != null) {
            hideEmptyIfShowing();
        }

        return view;
    }

    private void hideEmptyIfShowing() {
        if (empty.getVisibility() == View.VISIBLE) {
            empty.setVisibility(View.GONE);
        }
    }

    public void load() {
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adap = ImageRecyclerAdapter.create(key, (CragChatActivity) getActivity());
        RecyclerUtils.setAdapterAndManager(recyclerView, adap, LinearLayoutManager.VERTICAL);
        //recyclerView.setAdapter(adap);
        this.getLifecycle().addObserver(adap);
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
                //Log.d("RouteActivity", "Must be logged in to add an image");
                DialogFragment df = NotificationDialog.newInstance("Must be logged in to add an image");
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
                editImage.putExtra("displayable_id", key);
                Activity activity = getActivity();
                editImage.putExtra("entityType", activity instanceof RouteActivity ? "route" : "area");
                startActivity(editImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}