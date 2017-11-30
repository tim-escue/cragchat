package com.cragchat.mobile.view.adapters.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.model.Comment;
import com.cragchat.mobile.repository.Callback;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.util.FormatUtil;
import com.cragchat.mobile.view.adapters.recycler.viewholder.CommentRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

import static android.view.View.GONE;

/**
 * Created by timde on 10/20/2017.
 */

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerViewHolder> {

    private CommentRecyclerViewHolder lastOpened;
    private List<Comment> data;
    private String entityId;
    private String table;
    private Context context;
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

    public CommentRecyclerAdapter(Context context, List<Comment> comments, String table, String entityId) {
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
        List<Comment> comments = new ArrayList<>();
        for (Comment i : data) {
            if (comment == null || !i.getKey().equalsIgnoreCase(comment.getKey())) {
                comments.add(i);
            }
        }
        if (comment != null) {
            comments.add(comment);
        }
        data = arrangeComments(comments);
        notifyDataSetChanged();
    }

    @Override
    public CommentRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CommentRecyclerViewHolder(getItemView(viewGroup));
    }

    public static View getItemView(ViewGroup viewGroup) {
        return LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.comment_list_item, viewGroup, false);
    }

    public void sort(int j) {
        sort = j;
        updateSingle(null);
    }

    @Override
    public void onBindViewHolder(final CommentRecyclerViewHolder holder, int position) {
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
                            CommentSectionFragment.getReplyCommentDialog(
                                    CommentRecyclerAdapter.this,
                                    null,
                                    context,
                                    entityId,
                                    table,
                                    comment.getKey(),
                                    comment.getDepth() + 1,
                                    callback).show();
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
                        if (Authentication.isLoggedIn(context)) {
                            CommentSectionFragment.getAddCommentDialog(CommentRecyclerAdapter.this,
                                    comment.getComment(), context, entityId, table, comment, callback).show();
                        } else {
                            Toast.makeText(context, "Must be logged in to edit comment", Toast.LENGTH_LONG).show();
                        }
                        if (lastOpened != null) {
                            lastOpened.expandable.setVisibility(GONE);
                            lastOpened = null;
                        }
                    }
                });
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
            Map<String, List<Comment>> hMap = new HashMap<>();
            for (Comment i : data) {
                if (hMap.containsKey(i.getParentId())) {
                    hMap.get(i.getParentId()).add(i);
                } else {
                    List<Comment> list = new ArrayList<>();
                    list.add(i);
                    hMap.put(i.getParentId(), list);
                }
            }
            RealmList<Comment> comments = new RealmList<>();
            addChildrenFor(hMap, "", comments);
            return comments;
        }
        return data;
    }


    public void addChildrenFor(Map<String, List<Comment>> hMap, String key, List<Comment> newList) {
        List<Comment> list = hMap.get(key);
        if (list != null) {
            Collections.sort(list, new Comparator<Comment>() {
                @Override
                public int compare(Comment comment, Comment t1) {
                    if (sort == 0) {
                        return t1.getScore() - comment.getScore();
                    } else {
                        try {
                            long dif = FormatUtil.RAW_FORMAT.parse(comment.getDate()).getTime() - FormatUtil.RAW_FORMAT.parse(t1.getDate()).getTime();
                            System.out.println("DEF:" + dif);
                            if (dif > 0) {
                                return -1;
                            } else if (dif < 0) {
                                return 1;
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                }
            });
            for (Comment i : list) {
                newList.add(i);
                addChildrenFor(hMap, i.getKey(), newList);
            }
        }
    }

}
