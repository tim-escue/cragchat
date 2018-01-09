package com.cragchat.mobile.ui.view.adapters.recycler.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.model.Image;
import com.cragchat.mobile.ui.view.adapters.recycler.ImageRecyclerAdapter;
import com.cragchat.mobile.util.FileUtil;
import com.cragchat.mobile.util.FormatUtil;

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
    @BindView(R.id.entity)
    TextView entity;

    public ImageRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Image image, Context activity) {
        authorName.setText(image.getAuthorName());
        if (image.getCaption().isEmpty()) {
            caption.setVisibility(View.GONE);
        } else {
            caption.setVisibility(View.VISIBLE);
            caption.setText(image.getCaption());
        }
        entity.setText("Submitted for " + image.getEntityName());
        dateTextView.setText(FormatUtil.getDateAsElapsed(image.getDate()));

        FileUtil.loadIntoImageView(image.getFilename(), this.image, progressBar);

        imageCard.setOnClickListener(new ImageRecyclerAdapter.OpenImageListener(image, activity));
    }
}