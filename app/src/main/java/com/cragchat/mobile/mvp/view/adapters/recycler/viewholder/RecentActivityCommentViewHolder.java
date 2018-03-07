package com.cragchat.mobile.mvp.view.adapters.recycler.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.domain.model.Comment;
import com.cragchat.mobile.domain.util.FormatUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentActivityCommentViewHolder extends RecyclerView.ViewHolder {

    /*@BindView(R.id.comment_author)
    TextView text1;
    @BindView(R.id.comment_date)
    TextView text2;
    @BindView(R.id.comment_body)
    TextView text3;
    @BindView(R.id.comment_label)
    TextView label;

    @BindView(R.id.text_score)
    TextView text5;
    @BindView(R.id.layout_comment)
    RelativeLayout layout;

    public RecentActivityCommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static View getItemView(ViewGroup viewGroup, @LayoutRes int layout) {
        return LayoutInflater.
                from(viewGroup.getContext()).
                inflate(layout, viewGroup, false);
    }

    public void bind(final Comment comment) {
        /*
            Give the comment an inset if it has a parent.

        //int paddingPixel = 16;
        // float density = layout.getResources().getDisplayMetrics().density;
        // int paddingDp = (int) (paddingPixel * density);
        // layout.setPadding(comment.getDepth() * paddingDp, layout.getPaddingTop(), layout.getPaddingRight(), layout.getPaddingBottom());

        int score = comment.getScore();
        String points = "0 points";
        if (score < 0) {
            points = score + " points";
        } else if (score > 0) {
            points = "+" + score + " points";
        }
        text5.setText(points);

        text1.setText(comment.getAuthorName());
        label.setText(" posted " + comment.getTable().toLowerCase() + " for " + comment.getEntityName());
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (((CragChatActivity) activity).hasConnection()) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", comment.getAuthorName());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Must have internet connection to view user profiles.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String date = comment.getDate();
        Date dateObject = null;
        try {
            dateObject = FormatUtil.RAW_FORMAT.parse(comment.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        text2.setText(dateObject != null ? FormatUtil.elapsed(dateObject, Calendar.getInstance().getTime()) : date);
        text3.setText(comment.getComment());

    }
    */

    @BindView(R.id.comment_author)
    TextView text1;
    @BindView(R.id.comment_date)
    TextView text2;
    @BindView(R.id.comment_body)
    TextView text3;
    @BindView(R.id.comment_label)
    TextView label;

    @BindView(R.id.text_score)
    TextView text5;

    public RecentActivityCommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static View getItemView(ViewGroup viewGroup, @LayoutRes int layout) {
        return LayoutInflater.
                from(viewGroup.getContext()).
                inflate(layout, viewGroup, false);
    }

    public void bind(final Comment comment) {
        /*
            Give the comment an inset if it has a parent.
         */
        //int paddingPixel = 16;
        // float density = layout.getResources().getDisplayMetrics().density;
        // int paddingDp = (int) (paddingPixel * density);
        // layout.setPadding(comment.getDepth() * paddingDp, layout.getPaddingTop(), layout.getPaddingRight(), layout.getPaddingBottom());

        int score = comment.getScore();
        String points = "0 points";
        if (score < 0) {
            points = score + " points";
        } else if (score > 0) {
            points = "+" + score + " points";
        }
        text5.setText(points);

        text1.setText(comment.getAuthorName());
        String table = comment.getTable().toLowerCase();
        String entityName = comment.getEntityName();
        String labelString = table.equals("beta") ? "Added beta for " : "In discussion for ";
        labelString += entityName;

        label.setText(labelString);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (((CragChatActivity) activity).hasConnection()) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", comment.getAuthorName());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Must have internet connection to view user profiles.", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        String date = comment.getDate();
        Date dateObject = null;
        try {
            dateObject = FormatUtil.RAW_FORMAT.parse(comment.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        text2.setText(dateObject != null ? FormatUtil.elapsed(dateObject, Calendar.getInstance().getTime()) : date);
        text3.setText(comment.getComment());

    }
}