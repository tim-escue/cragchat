package com.cragchat.mobile.view.adapters.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.model.Comment;
import com.cragchat.mobile.model.Datable;
import com.cragchat.mobile.model.Image;
import com.cragchat.mobile.model.Rating;
import com.cragchat.mobile.model.Send;
import com.cragchat.mobile.util.FileUtil;
import com.cragchat.mobile.view.adapters.recycler.viewholder.ImageRecyclerViewHolder;
import com.cragchat.mobile.view.adapters.recycler.viewholder.RecentActivityCommentViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 7/25/17.
 */

public class RecentActivityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CragChatActivity activity;
    private List<Datable> data;


    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RATING = 2;
    private static final int TYPE_IMAGE = 3;

    public RecentActivityRecyclerAdapter(CragChatActivity a, List<Datable> data) {
        activity = a;
        if (data != null) {
            this.data = data;
        } else {
            this.data = new ArrayList<>();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_COMMENT:
                return new RecentActivityCommentViewHolder(RecentActivityCommentViewHolder.getItemView(parent));
            case TYPE_SEND:
                return new SendRecyclerAdapter.ViewHolder(SendRecyclerAdapter.getItemView(parent));
            case TYPE_RATING:
                return new RatingRecyclerAdapter.ViewHolder(RatingRecyclerAdapter.getItemView(parent));
            case TYPE_IMAGE:
                return new ImageRecyclerViewHolder(ImageRecyclerViewHolder.getItemViewRecentActivity(parent));
        }
        return null;
    }

    public void update(List<Datable> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = data.get(position);
        if (obj instanceof Comment) {
            return TYPE_COMMENT;
        } else if (obj instanceof Image) {
            return TYPE_IMAGE;
        } else if (obj instanceof Rating) {
            return TYPE_RATING;
        } else {
            return TYPE_SEND;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SendRecyclerAdapter.ViewHolder) {
            SendRecyclerAdapter.ViewHolder vh = (SendRecyclerAdapter.ViewHolder) holder;
            vh.bind((Send) data.get(position));
        } else if (holder instanceof RatingRecyclerAdapter.ViewHolder) {
            RatingRecyclerAdapter.ViewHolder vh = (RatingRecyclerAdapter.ViewHolder) holder;
            vh.bind((Rating) data.get(position), activity);
        } else if (holder instanceof ImageRecyclerViewHolder) {
            ImageRecyclerViewHolder vh = (ImageRecyclerViewHolder) holder;
            File album = FileUtil.getAlbumStorageDir("routedb");
            vh.bind((Image) data.get(position), album, activity);
        } else if (holder instanceof RecentActivityCommentViewHolder) {
            RecentActivityCommentViewHolder vh = (RecentActivityCommentViewHolder) holder;
            vh.bind((Comment) data.get(position));
        }
       /* final Object obj = activities.get(position);
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
                                new GrabImageTask(activity, LocalDatabase.getInstance(activity), img, holder.imageView, false).onSuccess();
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
                        if (disp instanceof LegacyRoute) {
                            activity.launch(disp, 5);
                        } else if (disp instanceof LegacyArea) {
                            activity.launch(disp, 4);
                        }
                    }
                });
                holder.text2.setVisibility(View.GONE);
            }
        }
        holder.text1.setText(Html.fromHtml(content));*/

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
