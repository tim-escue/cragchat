package com.cragchat.mobile.view.adapters.recycler;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.model.Comment;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.util.FormatUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by timde on 10/20/2017.
 */

public class CommentRecyclerAdapterTEST extends RecyclerView.Adapter<CommentRecyclerAdapterTEST.CommentRecyclerViewHolderTEST> {

    private CommentRecyclerViewHolderTEST lastOpened;
    private List<Comment> data;
    private String entityId;
    private String table;
    private Context context;
    private Map<String, List<Comment>> map;

    private Callback<Comment> callback = new Callback<Comment>() {
        @Override
        public void onSuccess(Comment object) {
            updateSingle(object);
        }

        @Override
        public void onFailure() {

        }
    };
    private int sort;

    public CommentRecyclerAdapterTEST(Context context, List<Comment> comments, String table, String entityId) {
        this.table = table;
        this.entityId = entityId;
        this.context = context;
        data = arrangeComments(comments);
    }

    public void update(List<Comment> comments) {
        data = arrangeComments(comments);
        notifyDataSetChanged();
    }

    public void updateSingle(Comment comment) {
        /*List<Comment> comments = new ArrayList<>();
        for (Comment i : data) {
            if (comment == null || !i.getKey().equalsIgnoreCase(comment.getKey())) {
                comments.add(i);
            }
        }
        if (comment != null) {
            comments.add(comment);
        }
        data = arrangeComments(comments);
        notifyDataSetChanged();*/
    }

    @Override
    public CommentRecyclerViewHolderTEST onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CommentRecyclerViewHolderTEST(getItemView(viewGroup));
    }

    public static View getItemView(ViewGroup viewGroup) {
        return LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.ccomment_lis_item_test, viewGroup, false);
    }

    public void sort(int j) {
        sort = j;
        updateSingle(null);
    }

    @Override
    public void onBindViewHolder(final CommentRecyclerViewHolderTEST holder, int position) {
        final Comment comment = data.get(position);
        Comment after = position < getItemCount() - 1 ? data.get(position + 1) : null;
        holder.bind(comment, after, position, lastOpened,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastOpened != null && !lastOpened.equals(holder)) {
                            lastOpened.expandable.setVisibility(GONE);
                        }
                        lastOpened = holder;
                        if (holder.expandable.getVisibility() == GONE) {
                            holder.expandable.setVisibility(View.VISIBLE);
                        } else {
                            holder.expandable.setVisibility(GONE);
                        }
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vote(comment.getKey(), true);
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vote(comment.getKey(), false);
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Authentication.isLoggedIn(context)) {
                            /*CommentSectionFragment.getReplyCommentDialog(
                                    CommentRecyclerAdapterTEST.this,
                                    null,
                                    context,
                                    entityId,
                                    table,
                                    comment.getKey(),
                                    comment.getDepth() + 1,
                                    callback).show();*/
                        } else {
                            Toast.makeText(context, "Must be logged in to reply", Toast.LENGTH_LONG).show();
                        }
                        if (lastOpened != null) {
                            lastOpened.expandable.setVisibility(GONE);
                            lastOpened = null;
                        }
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*
                        if (Authentication.isLoggedIn(context)) {
                            CommentSectionFragment.getAddCommentDialog(CommentRecyclerAdapter.this,
                                    comment.getComment(), context, entityId, table, comment, callback).show();
                        } else {
                            Toast.makeText(context, "Must be logged in to edit comment", Toast.LENGTH_LONG).show();
                        }
                        if (lastOpened != null) {
                            lastOpened.expandable.setVisibility(GONE);
                            lastOpened = null;
                        }*/
                    }
                });
        addChild(holder, comment);
    }

    private void vote(String commentKey, boolean up) {
        if (Authentication.isLoggedIn(context)) {
            Repository.addCommentVote(
                    Authentication.getAuthenticatedUser(context).getToken(),
                    up ? "up" : "down",
                    commentKey,
                    new Callback<Comment>() {
                        @Override
                        public void onSuccess(Comment object) {
                            updateSingle(object);
                        }

                        @Override
                        public void onFailure() {

                        }
                    }
            );
            if (lastOpened != null) {
                lastOpened.expandable.setVisibility(GONE);
                lastOpened = null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<Comment> arrangeComments(List<Comment> data) {
        if (data != null && data.size() > 0) {
            map = new HashMap<String, List<Comment>>();
            List<Comment> temp = new ArrayList<>();
            for (Comment i : data) {
                if (map.containsKey(i.getParentId())) {
                    map.get(i.getParentId()).add(i);
                } else {
                    List<Comment> list = new ArrayList<>();
                    list.add(i);
                    map.put(i.getParentId(), list);
                }
                if (i.getDepth() == 0) {
                    temp.add(i);

                }
            }
            return temp;
        }
        return data;
    }

    public class CommentRecyclerViewHolderTEST extends RecyclerView.ViewHolder {

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
        @BindView(R.id.expanding_layout)
        public LinearLayout expandable;
        @BindView(R.id.line_if_more)
        View lineIfMore;
        @BindView(R.id.subcomment_with_more)
        View subcommentWithMore;
        @BindView(R.id.child_container)
        ViewGroup group;

        public CommentRecyclerViewHolderTEST(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Comment comment, final Comment afterComment, final int position, final RecyclerView.ViewHolder lastOpened,
                         View.OnClickListener expandingClickListener, View.OnClickListener voteUpClickListener,
                         View.OnClickListener voteDownClickListener, View.OnClickListener replyClickListener,
                         View.OnClickListener addCommentListener) {
            //final int color = (position % 2 == 0) ? Color.TRANSPARENT : Color.argb(255, 225, 225, 225);
            //layout.setBackgroundColor(color);
            //expandable.setBackgroundColor(color);

            layout.setOnClickListener(expandingClickListener);

            if (lastOpened != null && equals(lastOpened)) {
                expandable.setVisibility(View.VISIBLE);
            } else {
                expandable.setVisibility(GONE);
            }

            if (comment.getDepth() != 0) {
                subcommentWithMore.setVisibility(View.VISIBLE);
                if (afterComment != null && afterComment.getDepth() == comment.getDepth()) {
                    lineIfMore.setBackgroundColor(Color.BLACK);
                } else {
                    lineIfMore.setBackgroundColor(Color.TRANSPARENT);
                }
            } else {
                subcommentWithMore.setVisibility(GONE);
            }

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

            if (Authentication.isLoggedIn(itemView.getContext()) &&
                    Authentication.getAuthenticatedUser(itemView.getContext())
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

    private void addChild(CommentRecyclerViewHolderTEST vh, Comment comment) {
        if (map.containsKey(comment.getKey())) {
            List<Comment> children = map.get(comment.getKey());
            for (Comment i : children) {
                View v = getItemView(vh.group);
                CommentRecyclerViewHolderTEST vhn = new CommentRecyclerViewHolderTEST(v);
                vhn.bind(i, null, 0, lastOpened, null, null, null, null, null);
                vh.group.addView(v);
                addChild(vhn, i);
            }
        }
    }

}
