package com.cragchat.mobile.ui.view.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Image;
import com.cragchat.mobile.ui.presenter.ImageFragmentPresenter;
import com.cragchat.mobile.ui.view.activity.EditImageActivity;
import com.cragchat.mobile.ui.view.activity.RouteActivity;
import com.cragchat.mobile.util.PermissionUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;

import static com.cragchat.mobile.util.NavigationUtil.ENTITY_KEY;


public class ImageFragment extends DaggerFragment implements View.OnClickListener {

    public static final int PICK_IMAGE = 873;

    @Inject
    @Named(InjectionNames.ENTITY_KEY)
    String entityKey;

    @Inject
    Repository mRepository;

    @Inject
    Authentication mAuthentication;

    private ImageFragmentPresenter presenter;

    @Inject
    public ImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        
        presenter = new ImageFragmentPresenter(view, getLifecycle(), entityKey);

        mRepository.observeImages(entityKey).subscribe(images -> presenter.present(images));
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_button) {
            int permissionWriteExternal = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionWriteExternal == PackageManager.PERMISSION_GRANTED) {
                if (mAuthentication.isLoggedIn(getContext())) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                } else {
                    DialogFragment df = NotificationDialog.newInstance("Must be logged in to add an image.");
                    df.show(getFragmentManager(), "dialog");
                }
            } else {
                PermissionUtil.requestPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        "Crag Chat needs permission to access your external storage to add images.");
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
                editImage.putExtra(ENTITY_KEY, entityKey);
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