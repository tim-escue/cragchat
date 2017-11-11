package com.cragchat.mobile.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.cragchat.mobile.model.realm.RealmImage;
import com.cragchat.mobile.network.Network;
import com.cragchat.mobile.repository.remote.ErrorHandlingObserverable;
import com.cragchat.mobile.repository.remote.RetroFitRestApi;
import com.cragchat.mobile.view.adapters.recycler.ImageRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;


public class ImageFragment extends Fragment implements View.OnClickListener {

    public static final int PICK_IMAGE = 873;
    private String key;
    private ImageRecyclerAdapter adap;
    private Realm mRealm;

    @BindView(R.id.list_empty)
    TextView empty;
    @BindView(R.id.images_recycler)
    RecyclerView recyclerView;

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

        mRealm = Realm.getDefaultInstance();

        key = getArguments().getString("id");

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        load();

        if (Network.isConnected(getContext())) {
            RetroFitRestApi.getInstance().getImages(key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorHandlingObserverable<List<RealmImage>>() {
                        @Override
                        public void onSuccess(final List<RealmImage> object) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.insertOrUpdate(object);
                                }
                            });
                            realm.close();
                            adap.updateData(mRealm.where(RealmImage.class).equalTo(RealmImage.FIELD_ENTITY_KEY, key).findAll());
                            if (object.size() > 0) {
                                empty.setVisibility(View.GONE);
                            }

                        }
                    });
        }


        return view;
    }

    public void load() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        RealmResults<RealmImage> images = mRealm.where(RealmImage.class).equalTo(RealmImage.FIELD_ENTITY_KEY, key).findAll();
        adap = new ImageRecyclerAdapter(images, true, (CragChatActivity) getActivity());
        recyclerView.setAdapter(adap);
        if (images.size() > 0) {
            empty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
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