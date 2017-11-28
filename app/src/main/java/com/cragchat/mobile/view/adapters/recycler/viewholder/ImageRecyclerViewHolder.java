package com.cragchat.mobile.view.adapters.recycler.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cragchat.mobile.R;
import com.cragchat.mobile.model.Image;
import com.cragchat.mobile.view.GlideApp;
import com.cragchat.mobile.view.adapters.recycler.ImageRecyclerAdapter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageRecyclerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.caption)
    public TextView caption;
    @BindView(R.id.image)
    public ImageView image;
    @BindView(R.id.author_name)
    public TextView authorName;
    @BindView(R.id.image_card)
    public View imageCard;
    @BindView(R.id.image_progress)
    public ProgressBar progressBar;

    public ImageRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static View getItemViewRecentActivity(ViewGroup parent) {
        return LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.recent_activity_image, parent, false);
    }

    public void bind(Image realmImage, File album, Context activity) {
        authorName.setText(realmImage.getAuthorName());
        if (realmImage.getCaption().isEmpty()) {
            caption.setVisibility(View.GONE);
        } else {
            caption.setVisibility(View.VISIBLE);
            caption.setText(realmImage.getCaption());
        }

        final String localPath = album.getPath() + "/" + realmImage.getFilename();
        final String remotePath = "http://ec2-54-148-84-77.us-west-2.compute.amazonaws.com/static/" + realmImage.getFilename();

        File localImage = new File(localPath);

        if (localImage.exists()) {
            GlideApp.with(activity)
                    .load(localImage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(image);
            imageCard.setOnClickListener(new ImageRecyclerAdapter.OpenImageListener(realmImage.getFilename(), localPath, realmImage.getEntityKey(), activity));
        } else {
            GlideApp.with(activity)
                    .load(R.drawable.tap_to_load)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(image);
            imageCard.setOnClickListener(new ImageRecyclerAdapter.LoadImageListener(this, realmImage.getFilename(), localPath, remotePath, realmImage.getEntityKey(), activity));
        }
    }
}