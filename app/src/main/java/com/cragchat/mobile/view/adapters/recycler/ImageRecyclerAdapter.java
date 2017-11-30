package com.cragchat.mobile.view.adapters.recycler;

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
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.ViewImageActivity;
import com.cragchat.mobile.model.realm.RealmImage;
import com.cragchat.mobile.task.GetImageTask;
import com.cragchat.mobile.task.TaskCallback;
import com.cragchat.mobile.util.FileUtil;
import com.cragchat.mobile.view.adapters.recycler.viewholder.ImageRecyclerViewHolder;

import java.io.File;
import java.io.FileOutputStream;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class ImageRecyclerAdapter extends RealmRecyclerViewAdapter<RealmImage, ImageRecyclerViewHolder> implements LifecycleObserver {

    private CragChatActivity activity;
    private File album;
    private Realm mRealm;
    private String entityKey;

    public static ImageRecyclerAdapter create(String entityKey, CragChatActivity activity) {
        Realm realm = Realm.getDefaultInstance();
        return new ImageRecyclerAdapter(
                realm.where(RealmImage.class).equalTo(RealmImage.FIELD_ENTITY_KEY, entityKey).findAll(),
                true, activity, entityKey, realm);
    }

    private ImageRecyclerAdapter(@Nullable OrderedRealmCollection<RealmImage> data
            , boolean autoUpdate, CragChatActivity activity, String entityKey, Realm realm) {
        super(data, autoUpdate);
        this.activity = activity;
        album = FileUtil.getAlbumStorageDir("routedb");
        this.mRealm = realm;
        this.entityKey = entityKey;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void disconnectListener() {
        System.out.println("Lifecycle: Actually destroyed images");
        mRealm.close();
    }


    @Override
    public ImageRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageRecyclerViewHolder(getItemView(parent));
    }

    public static View getItemView(ViewGroup parent) {
        return LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.recent_activity_image, parent, false);
    }

    @Override
    public void onBindViewHolder(final ImageRecyclerViewHolder holder, int position) {
        final RealmImage image = getItem(position);
        if (image != null) {
            holder.bind(image, album, activity);
        }
    }

    public static class OpenImageListener implements View.OnClickListener {
        private String localPath;
        private String imageFileName;
        private String entityKey;
        private Context activity;

        public OpenImageListener(String imageFileName, String localPath, String entityKey, Context activity) {
            this.localPath = localPath;
            this.imageFileName = imageFileName;
            this.entityKey = entityKey;
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, ViewImageActivity.class);
            intent.putExtra(RealmImage.FIELD_FILENAME, imageFileName);
            intent.putExtra("localPath", localPath);
            intent.putExtra("entityKey", entityKey);
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
            TaskCallback<Bitmap> callback = new TaskCallback<Bitmap>() {
                @Override
                public void onResult(Bitmap result) {
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
                    holder.imageCard.setOnClickListener(new OpenImageListener(imageKey, localPath, entityKey, activity));
                    holder.image.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.GONE);
                    if (!holder.caption.getText().toString().isEmpty()) {
                        holder.caption.setVisibility(View.VISIBLE);
                    }
                }
            };
            new GetImageTask(remotePath, callback).execute();
        }

    }

}
