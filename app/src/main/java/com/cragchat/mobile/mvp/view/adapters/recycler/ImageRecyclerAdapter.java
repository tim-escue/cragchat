package com.cragchat.mobile.mvp.view.adapters.recycler;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cragchat.mobile.R;
import com.cragchat.mobile.data.Callback;
import com.cragchat.mobile.data.remote.GetImageTask;
import com.cragchat.mobile.domain.model.Image;
import com.cragchat.mobile.data.local.realm.RealmImage;
import com.cragchat.mobile.mvp.view.activity.ViewImageActivity;
import com.cragchat.mobile.mvp.view.adapters.recycler.viewholder.ImageRecyclerViewHolder;
import com.cragchat.mobile.util.ViewUtil;

import java.io.File;
import java.io.FileOutputStream;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

public class ImageRecyclerAdapter extends RealmRecyclerViewAdapter<RealmImage, ImageRecyclerViewHolder> implements LifecycleObserver {

    private Context activity;
    private Realm mRealm;

    private ImageRecyclerAdapter(@Nullable OrderedRealmCollection<RealmImage> data
            , boolean autoUpdate, Context context, Realm realm) {
        super(data, autoUpdate);
        this.activity = context;
        this.mRealm = realm;
    }

    public static ImageRecyclerAdapter create(String entityKey, Context activity) {
        Realm realm = Realm.getDefaultInstance();
        return new ImageRecyclerAdapter(
                realm.where(RealmImage.class).equalTo(RealmImage.FIELD_ENTITY_KEY, entityKey).findAll().sort(RealmImage.FIELD_DATE, Sort.DESCENDING),
                true, activity, realm);
    }

    public static View getItemView(ViewGroup parent) {
        return LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.recent_activity_image, parent, false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void disconnectListener() {
        mRealm.close();
    }

    @Override
    public ImageRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageRecyclerViewHolder(getItemView(parent));
    }

    @Override
    public void onBindViewHolder(final ImageRecyclerViewHolder holder, int position) {
        final Image image = getItem(position);
        if (image != null) {
            holder.bind(image, activity);
        }
    }

    public static class OpenImageListener implements View.OnClickListener {
        private Image image;
        private Context activity;

        public OpenImageListener(Image image, Context activity) {
            this.activity = activity;
            this.image = image;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, ViewImageActivity.class);
            intent.putExtra(ViewUtil.IMAGE, image);
            activity.startActivity(intent);
        }

    }

    public static class LoadImageListener implements View.OnClickListener {

        private ImageRecyclerViewHolder holder;
        private String localPath;
        private String remotePath;
        private String imageKey;
        private String entityKey;
        private Context activity;

        public LoadImageListener(ImageRecyclerViewHolder holder, String imageKey, String localPath, String remotePath, String entityKey, Context activity) {
            this.holder = holder;
            this.localPath = localPath;
            this.remotePath = remotePath;
            this.imageKey = imageKey;
            this.activity = activity;
            this.entityKey = entityKey;
        }

        @Override
        public void onClick(View view) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.caption.setVisibility(View.GONE);
            Callback<Bitmap> callback = result -> {
                    File f = new File(localPath);
                    FileOutputStream stream = null;
                    try {
                        if (f.createNewFile()) {
                            stream = new FileOutputStream(f);
                            result.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (stream != null) {
                                stream.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    holder.image.setImageBitmap(result);
                    //holder.imageCard.setOnClickListener(new OpenImageListener(image, activity));
                    holder.image.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.GONE);
                    if (!holder.caption.getText().toString().isEmpty()) {
                        holder.caption.setVisibility(View.VISIBLE);
                    }

            };
            new GetImageTask(remotePath, callback).execute();
        }

    }

}
