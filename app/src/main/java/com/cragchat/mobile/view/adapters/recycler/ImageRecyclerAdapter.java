package com.cragchat.mobile.view.adapters.recycler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.ViewImageActivity;
import com.cragchat.mobile.model.realm.RealmImage;
import com.cragchat.mobile.task.GetImageTask;
import com.cragchat.mobile.task.TaskCallback;
import com.cragchat.mobile.util.FileUtil;
import com.cragchat.mobile.view.GlideApp;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ImageRecyclerAdapter extends RealmRecyclerViewAdapter<RealmImage, ImageRecyclerAdapter.ViewHolder> {

    private CragChatActivity activity;
    private File album;

    public ImageRecyclerAdapter(@Nullable OrderedRealmCollection<RealmImage> data, boolean autoUpdate, CragChatActivity activity) {
        super(data, autoUpdate);
        this.activity = activity;
        album = FileUtil.getAlbumStorageDir("routedb");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.image_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RealmImage image = getItem(position);
        if (image != null) {
            holder.authorName.setText(image.getAuthorName());
            if (image.getCaption().isEmpty()) {
                holder.caption.setVisibility(View.GONE);
            } else {
                holder.caption.setVisibility(View.VISIBLE);
                holder.caption.setText(image.getCaption());
            }

            final String localPath = album.getPath() + "/" + image.getFilename();
            final String remotePath = "http://ec2-54-148-84-77.us-west-2.compute.amazonaws.com/static/" + image.getFilename();

            File localImage = new File(localPath);

            if (localImage.exists()) {
                GlideApp.with(activity)
                        .load(localImage)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.image);
                holder.imageCard.setOnClickListener(new OpenImageListener(image.getFilename(), localPath, image.getEntityKey()));
            } else {
                GlideApp.with(activity)
                        .load(R.drawable.tap_to_load)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.image);
                holder.imageCard.setOnClickListener(new LoadImageListener(holder, image.getFilename(), localPath, remotePath, image.getEntityKey()));
            }
        }
    }

    class OpenImageListener implements View.OnClickListener {
        private String localPath;
        private String imageFileName;
        private String entityKey;

        OpenImageListener(String imageFileName, String localPath, String entityKey) {
            this.localPath = localPath;
            this.imageFileName = imageFileName;
            this.entityKey = entityKey;
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

    class LoadImageListener implements View.OnClickListener {

        private ViewHolder holder;
        private String localPath;
        private String remotePath;
        private String imageKey;
        private String entityKey;

        public LoadImageListener(ViewHolder holder, String imageKey, String localPath, String remotePath, String entityKey) {
            this.holder = holder;
            this.localPath = localPath;
            this.remotePath = remotePath;
            this.imageKey = imageKey;
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
                    holder.imageCard.setOnClickListener(new OpenImageListener(imageKey, localPath, entityKey));
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


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.caption)
        TextView caption;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.author_name)
        TextView authorName;
        @BindView(R.id.image_card)
        CardView imageCard;
        @BindView(R.id.image_progress)
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
