package chat.crag.cragchat.adapters;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import chat.crag.cragchat.CragChatActivity;
import chat.crag.cragchat.R;
import chat.crag.cragchat.comments.Comment;
import chat.crag.cragchat.descriptor.*;
import chat.crag.cragchat.sql.GrabImageTask;
import chat.crag.cragchat.sql.LocalDatabase;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class RecentActivityAdapter extends BaseAdapter {

    private CragChatActivity activity;
    private static LayoutInflater inflater;
    private List<Object> activities;

    public RecentActivityAdapter(CragChatActivity a, List<Object> activities) {
        activity = a;
        inflater = activity.getLayoutInflater();
        this.activities = activities;
    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Object getItem(int position) {
        return activities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        LinearLayout layout;
        TextView text1;
        TextView text2;
        LinearLayout content;
        ImageView imageView;
        ProgressBar progress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.item_list_recent_activity, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text1 = (TextView) vi.findViewById(R.id.text_recent_activity);
            holder.text2 = (TextView) vi.findViewById(R.id.text_recent_activity_text);
            holder.layout = (LinearLayout) vi.findViewById(R.id.layout_recent_activity);
            holder.content = (LinearLayout) vi.findViewById(R.id.item_recent_contentbox);
            holder.imageView = (ImageView) vi.findViewById(R.id.image_recent_activity);
            holder.progress = (ProgressBar) vi.findViewById(R.id.progress_image_load);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.content.setVisibility(View.VISIBLE);
        final Object obj = activities.get(position);
        String content = "null";
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
                content = "<font color='#33A5FF'>" +rating.getUserName() + "</font>" + " rated " + "<font color='#77AA00'>" +disp.getName()+"</font>" ;
                holder.text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.launch(disp, 2);

                    }
                });
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
                content = "<font color='#33A5FF'>" +comment.getAuthorName() + "</font>" + " posted " + comment.getTable().toLowerCase() + " for " + "<font color='#77AA00'>" +disp.getName()+"</font>";
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
            holder.content.setVisibility(View.GONE);
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

        return vi;

    }
}
