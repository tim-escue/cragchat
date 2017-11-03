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
import com.cragchat.mobile.database.models.RealmComment;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.util.FormatUtil;
import com.cragchat.networkapi.ErrorHandlingObserverable;
import com.cragchat.networkapi.NetworkApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;

import static android.view.View.GONE;

/**
 * Created by timde on 10/20/2017.
 */

public class NewCommentRecyclerAdapter extends RecyclerView.Adapter<NewCommentRecyclerAdapter.ViewHolder> {

    private ViewHolder lastOpened;
    private List<RealmComment> data;
    private String entityId;
    private String table;
    private Context context;

    public NewCommentRecyclerAdapter(Context context, List<RealmComment> comments, String table, String entityId) {
        this.table = table;
        this.entityId = entityId;
        this.context = context;
        data = arrangeComments(comments);
    }

    public void update(List<RealmComment> comments) {
        data = arrangeComments(comments);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.comment_list_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int color = (position % 2 == 0) ? Color.TRANSPARENT : Color.argb(255, 225, 225, 225);
        holder.layout.setBackgroundColor(color);
        holder.expandable.setBackgroundColor(color);
        final RealmComment comment = data.get(position);

        holder.layout.setOnClickListener(new View.OnClickListener() {
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
        });

        if (holder.equals(lastOpened)) {
            holder.expandable.setVisibility(View.VISIBLE);
        } else {
            holder.expandable.setVisibility(GONE);
        }

        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote(comment.getKey(), true);
            }
        });

        holder.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote(comment.getKey(), false);
            }
        });

        /*
            Give the comment an inset if it has a parent.
         */
        int paddingPixel = 16;
        float density = holder.layout.getResources().getDisplayMetrics().density;
        int paddingDp = (int) (paddingPixel * density);
        holder.layout.setPadding(comment.getDepth() * paddingDp, holder.layout.getPaddingTop(), holder.layout.getPaddingRight(), holder.layout.getPaddingBottom());

        int score = comment.getScore();
        String points = "0 points";
        if (score < 0) {
            points = score + " points";
        } else if (score > 0) {
            points = "+" + score + " points";
        }
        holder.text5.setText(points);

        if (Authentication.isLoggedIn(context) &&
                Authentication.getAuthenticatedUser(context)
                        .getName().equals(comment.getAuthorName())) {
            holder.text6.setVisibility(View.VISIBLE);
        } else {
            holder.text6.setVisibility(GONE);
        }
        holder.text1.setText(String.valueOf(comment.getAuthorName()));
        holder.text1.setOnClickListener(new View.OnClickListener() {
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

        holder.text2.setText(dateObject != null ? FormatUtil.elapsed(dateObject, Calendar.getInstance().getTime()) : date);
        holder.text3.setText(comment.getComment());
        holder.text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Authentication.isLoggedIn(context)) {
                    CommentSectionFragment.getReplyCommentDialog(
                            NewCommentRecyclerAdapter.this,
                            null,
                            context,
                            entityId,
                            table,
                            comment.getKey(),
                            comment.getDepth() + 1).show();
                } else {
                    Toast.makeText(context, "Must be logged in to reply", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Authentication.isLoggedIn(context)) {
                    CommentSectionFragment.getAddCommentDialog(
                            NewCommentRecyclerAdapter.this,
                            comment.getComment(),
                            context,
                            entityId,
                            table,
                            comment).show();
                } else {
                    Toast.makeText(context, "Must be logged in to edit comment", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void vote(String commentKey, boolean up) {
        if (Authentication.isLoggedIn(context)) {
            if (NetworkApi.isConnected(context)) {
                NetworkApi.getInstance().postCommentVote(
                        Authentication.getAuthenticatedUser(context).getToken(),
                        up ? "up" : "down",
                        commentKey
                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ErrorHandlingObserverable<RealmComment>() {
                            @Override
                            public void onSuccess(final RealmComment realmComment) {
                                Realm realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.insertOrUpdate(realmComment);
                                    }
                                });
                                final List<RealmComment> copied = realm.copyFromRealm(realm.where(RealmComment.class)
                                        .equalTo(RealmComment.FIELD_ENTITY_ID, entityId)
                                        .equalTo(RealmComment.FIELD_TABLE, table)
                                        .findAll());
                                realm.close();
                                update(copied);
                            }
                        });
            } else {
                Toast.makeText(context, "Must be online to postCommentVote", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static List<RealmComment> arrangeComments(List<RealmComment> data) {
        if (data != null && data.size() > 0) {
            long begin = System.currentTimeMillis();
            //Map children to parentId
            Map<String, List<RealmComment>> hMap = new HashMap<>();
            for (RealmComment i : data) {
                if (hMap.containsKey(i.getParentId())) {
                    hMap.get(i.getParentId()).add(i);
                } else {
                    List<RealmComment> list = new ArrayList<>();
                    list.add(i);
                    hMap.put(i.getParentId(), list);
                }
            }
            RealmList<RealmComment> comments = new RealmList<>();
            addChildrenFor(hMap, "", comments);
            System.out.println("FINISHED IN:" + (System.currentTimeMillis() - begin));
            return comments;
        }
        return data;
    }


    public static void addChildrenFor(Map<String, List<RealmComment>> hMap, String key, RealmList<RealmComment> newList) {
        List<RealmComment> list = hMap.get(key);
        if (list != null) {
            for (RealmComment i : list) {
                newList.add(i);
                addChildrenFor(hMap, i.getKey(), newList);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
        LinearLayout expandable;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
