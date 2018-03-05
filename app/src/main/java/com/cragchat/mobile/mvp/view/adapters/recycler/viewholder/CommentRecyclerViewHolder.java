package com.cragchat.mobile.mvp.view.adapters.recycler.viewholder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.data.authentication.Authentication;
import com.cragchat.mobile.domain.model.Comment;
import com.cragchat.mobile.util.FormatUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class CommentRecyclerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.expanding_layout)
    public LinearLayout expandable;
    @BindView(R.id.comment_author)
    TextView text1;
    @BindView(R.id.comment_date)
    TextView text2;
    @BindView(R.id.comment_body)
    TextView text3;
    @BindView(R.id.reply)
    TextView text4;
    @BindView(R.id.text_score)
    TextView text5;
    @BindView(R.id.edit_comment)
    TextView text6;
    @BindView(R.id.arrow_up)
    ImageView image1;
    @BindView(R.id.arrow_down)
    ImageView image2;
    @BindView(R.id.layout_comment)
    LinearLayout layout;
    @BindView(R.id.line_if_more)
    View lineIfMore;
    @BindView(R.id.subcomment_with_more)
    View subcommentWithMore;

    Authentication mAuthentication;

    public CommentRecyclerViewHolder(View itemView, Authentication authentication) {
        super(itemView);
        this.mAuthentication = authentication;
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Comment comment, final Comment afterComment, final int position, final RecyclerView.ViewHolder lastOpened,
                     View.OnClickListener expandingClickListener, View.OnClickListener voteUpClickListener,
                     View.OnClickListener voteDownClickListener, View.OnClickListener replyClickListener,
                     View.OnClickListener addCommentListener) {
        final int color = (position % 2 == 0) ? Color.TRANSPARENT : Color.argb(255, 225, 225, 225);
        layout.setBackgroundColor(color);
        expandable.setBackgroundColor(color);

        layout.setOnClickListener(expandingClickListener);

        if (lastOpened != null && equals(lastOpened)) {
            expandable.setVisibility(View.VISIBLE);
        } else {
            expandable.setVisibility(GONE);
        }

       /* if (comment.getDepth() != 0) {
            subcommentWithMore.setVisibility(View.VISIBLE);
            if (afterComment != null && afterComment.getDepth() == comment.getDepth()) {
                lineIfMore.setBackgroundColor(Color.BLACK);
            } else {
                lineIfMore.setBackgroundColor(Color.TRANSPARENT);
            }
        } else {
            subcommentWithMore.setVisibility(GONE);
        }*/
        subcommentWithMore.setVisibility(GONE);

        image1.setOnClickListener(voteUpClickListener);

        image2.setOnClickListener(voteDownClickListener);

        /*
            Give the comment an inset if it has a parent.
         */
        int paddingPixel = 16;
        float density = layout.getResources().getDisplayMetrics().density;
        int paddingDp = (int) (paddingPixel * density);
        layout.setPadding(comment.getDepth() * paddingDp, layout.getPaddingTop(), layout.getPaddingRight(), layout.getPaddingBottom());

        int score = comment.getScore();
        String points = "0 points";
        if (score < 0) {
            points = score + " points";
        } else if (score > 0) {
            points = "+" + score + " points";
        }
        text5.setText(points);

        if (mAuthentication.isLoggedIn(itemView.getContext()) &&
                mAuthentication.getAuthenticatedUser(itemView.getContext())
                        .getName().equals(comment.getAuthorName())) {
            text6.setVisibility(View.VISIBLE);
        } else {
            text6.setVisibility(GONE);
        }
        text1.setText(String.valueOf(comment.getAuthorName()));
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
        text4.setOnClickListener(replyClickListener);
        text6.setOnClickListener(addCommentListener);
    }
}