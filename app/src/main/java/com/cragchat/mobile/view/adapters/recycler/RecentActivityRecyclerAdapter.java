package com.cragchat.mobile.view.adapters.recycler;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Image;
import com.cragchat.mobile.descriptor.Rating;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.sql.GrabImageTask;
import com.cragchat.mobile.sql.LocalDatabase;

import java.io.File;
import java.util.List;

/**
 * Created by tim on 7/25/17.
 */

public class RecentActivityRecyclerAdapter extends RecyclerView.Adapter<RecentActivityRecyclerAdapter.DisplayableHolder> {

    private CragChatActivity activity;
    private List<Object> activities;

    public RecentActivityRecyclerAdapter(CragChatActivity a, List<Object> activities) {
        activity = a;
        this.activities = activities;
    }

    @Override
    public DisplayableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_list_recent_activity, parent, false);
        return new DisplayableHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DisplayableHolder holder, int position) {
        final Object obj = activities.get(position);
        String content = "null";
        holder.divider.setVisibility(View.VISIBLE);
        if (obj instanceof Rating) {
            holder.imageView.setVisibility(View.GONE);

            Rating rating = (Rating) obj;
            final Displayable disp = LocalDatabase.getInstance(activity).findExact(rating.getRouteId());
            if (disp == null) {
                holder.text1.setVisibility(View.GONE);
                holder.layout.setVisibility(View.GONE);
            } else {
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.launch(disp, 2);

                    }
                });
                content = "<font color='#33A5FF'>" + rating.getUserName() + "</font>" + " rated " + "<font color='#77AA00'>" + disp.getName() + "</font>";

                String ratingStrin = "YDS: \t" + disp.getYdsString(activity, rating.getYds()) + "\nStars: \t" + rating.getStars();
                holder.text2.setVisibility(View.VISIBLE);

                holder.text2.setText(ratingStrin);
            }


        } else if (obj instanceof Comment) {
            holder.imageView.setVisibility(View.GONE);

            Comment comment = (Comment) obj;
            final Displayable disp = LocalDatabase.getInstance(activity).findExact(comment.getDisplayId());
            if (disp == null) {
                holder.text1.setVisibility(View.GONE);
                holder.layout.setVisibility(View.GONE);
            } else {
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.launch(disp, 0);

                    }
                });
                content = "<font color='#33A5FF'>" + comment.getAuthorName() + "</font>" + " posted " + comment.getTable().toLowerCase() + " for " + "<font color='#77AA00'>" + disp.getName() + "</font>";
                holder.text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.launch(disp, 0);

                    }
                });

                String text = comment.getText();
                if (text.length() > 100) {
                    text = text.substring(0, 100);
                    if (text.contains(" ")) {
                        text = text.substring(0, text.lastIndexOf(' '));
                    }
                    text += "...";
                }
                holder.text2.setVisibility(View.VISIBLE);

                holder.text2.setText(text);
            }

        } else if (obj instanceof Image) {
            holder.text2.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
            final Image img = (Image) obj;
            final Displayable disp = LocalDatabase.getInstance(activity).findExact(img.getDisplayId());
            if (disp == null) {
                holder.text1.setVisibility(View.GONE);
                holder.layout.setVisibility(View.GONE);
            } else {

                File album = Image.getAlbumStorageDir("routedb");
                File save = new File(album.getPath() + "/" + img.getName());
                if (save.exists()) {
                    holder.imageView.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(save).into(holder.imageView);
                } else {
                    holder.imageView.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(R.drawable.tap_to_load).into(holder.imageView);
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int permissionWriteExternal = ContextCompat.checkSelfPermission(activity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionWriteExternal == PackageManager.PERMISSION_GRANTED) {
                                holder.imageView.setVisibility(View.GONE);
                                holder.progress.setVisibility(View.VISIBLE);
                                new GrabImageTask(activity, LocalDatabase.getInstance(activity), img, holder.imageView, false).execute();
                            } else {
                                Toast.makeText(activity, "App needs permission to Write To External Storage to load images.", Toast.LENGTH_SHORT).show();
                            }
                            //System.out.println("loading image");
                            holder.imageView.setOnClickListener(null);

                        }
                    });
                }

                content = "<font color='#33A5FF'>" + img.getAuthor() + "</font>" + " posted an image for " + "<font color='#77AA00'>" + disp.getName() + "</font>";
                holder.text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (disp instanceof Route) {
                            activity.launch(disp, 5);
                        } else if (disp instanceof Area) {
                            activity.launch(disp, 4);
                        }
                    }
                });
                holder.text2.setVisibility(View.GONE);
            }
        }
        holder.text1.setText(Html.fromHtml(content));

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class DisplayableHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView text1;
        TextView text2;
        ImageView imageView;
        ProgressBar progress;
        View divider;

        public DisplayableHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text_recent_activity);
            text2 = (TextView) itemView.findViewById(R.id.text_recent_activity_text);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_recent_activity);
            imageView = (ImageView) itemView.findViewById(R.id.image_recent_activity);
            progress = (ProgressBar) itemView.findViewById(R.id.progress_image_load);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
