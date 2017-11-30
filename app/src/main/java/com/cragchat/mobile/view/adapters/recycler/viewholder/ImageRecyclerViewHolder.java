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
import com.cragchat.mobile.util.FormatUtil;
import com.cragchat.mobile.view.GlideApp;
import com.cragchat.mobile.view.adapters.recycler.ImageRecyclerAdapter;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

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
    @BindView(R.id.date)
    public TextView dateTextView;

    public ImageRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static View getItemViewRecentActivity(ViewGroup parent) {
        return LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.recent_activity_image, parent, false);
    }

    public void bind(Image image, File album, Context activity) {
        authorName.setText(image.getAuthorName());
        if (image.getCaption().isEmpty()) {
            caption.setVisibility(View.GONE);
        } else {
            caption.setVisibility(View.VISIBLE);
            caption.setText(image.getCaption());
        }
        String date = image.getDate();
        Date dateObject = null;
        try {
            dateObject = FormatUtil.RAW_FORMAT.parse(image.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        dateTextView.setText(dateObject != null ? FormatUtil.elapsed(dateObject, Calendar.getInstance().getTime()) : date);

        final String localPath = album.getPath() + "/" + image.getFilename();
        final String remotePath = "http://ec2-54-148-84-77.us-west-2.compute.amazonaws.com/static/" + image.getFilename();

        File localImage = new File(localPath);

        if (localImage.exists()) {
            GlideApp.with(activity)
                    .load(localImage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(this.image);
            imageCard.setOnClickListener(new ImageRecyclerAdapter.OpenImageListener(image.getFilename(), localPath, image.getEntityKey(), activity));
        } else {
            GlideApp.with(activity)
                    .load(R.drawable.tap_to_load)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(this.image);
            imageCard.setOnClickListener(new ImageRecyclerAdapter.LoadImageListener(this, image.getFilename(), localPath, remotePath, image.getEntityKey(), activity));
        }
    }
}