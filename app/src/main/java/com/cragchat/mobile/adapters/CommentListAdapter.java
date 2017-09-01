package com.cragchat.mobile.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.ProfileActivity;
import com.cragchat.mobile.activity.SearchActivity;
import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.comments.CommentManager;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.sql.SendCommentEditTask;
import com.cragchat.mobile.sql.SendCommentTask;
import com.cragchat.mobile.sql.VoteTask;
import com.cragchat.mobile.user.User;
import com.cragchat.mobile.util.FormatUtil;

import java.util.Date;

import static android.view.View.GONE;

public class CommentListAdapter extends BaseAdapter {

    private Activity activity;
    private CommentManager manager;
    private static LayoutInflater inflater;
    private String table;
    private ViewHolder lastOpened;

    public CommentListAdapter(Activity a, CommentManager manager, String table) {
        activity = a;
        this.manager = manager;
        inflater = activity.getLayoutInflater();
        this.table = table;
    }

    public void addComment(Comment m) {
        if (manager.getCommentList().size() == 0) {
            activity.findViewById(R.id.list_empty).setVisibility(GONE);
        }
        manager.addComment(m);
    }

    @Override
    public int getCount() {
        return manager.getCommentList().size();
    }

    @Override
    public Object getItem(int position) {
        return manager.getCommentList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void edit(Comment m) {
        for (Comment i : manager.getCommentList()) {
            if (i.getId() == m.getId()) {
                i.setText(m.getText());
            }
        }
    }

    public static class ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
        TextView text6;
        ImageView image1;
        ImageView image2;
        LinearLayout layout;
        LinearLayout expandable;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.comment_list_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text1 = (TextView) vi.findViewById(R.id.comment_author);
            holder.text2 = (TextView) vi.findViewById(R.id.comment_date);
            holder.text3 = (TextView) vi.findViewById(R.id.comment_body);
            holder.text4 = (TextView) vi.findViewById(R.id.reply);
            holder.text5 = (TextView) vi.findViewById(R.id.text_score);
            holder.image1 = (ImageView) vi.findViewById(R.id.arrow_up);
            holder.image2 = (ImageView) vi.findViewById(R.id.arrow_down);
            holder.text6 = (TextView) vi.findViewById(R.id.edit_comment);
            holder.layout = (LinearLayout) vi.findViewById(R.id.layout_comment);
            holder.expandable = (LinearLayout) vi.findViewById(R.id.expanding_layout);


            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();
        int color = (position % 2 == 0) ? Color.TRANSPARENT : Color.argb(255, 225, 225, 225);
        holder.layout.setBackgroundColor(color);
        holder.expandable.setBackgroundColor(color);


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


        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.currentToken(activity) != null) {
                    new VoteTask(activity, manager.getCommentList().get(position), true, holder.text5).execute();
                } else {
                    Toast.makeText(activity, "Must be logged in to vote on comments", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.currentToken(activity) != null) {
                    new VoteTask(activity, manager.getCommentList().get(position), false, holder.text5).execute();
                } else {
                    Toast.makeText(activity, "Must be logged in to vote on comments", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
            Give the comment an inset if it has a parent.
         */
        int paddingPixel = 16;
        float density = activity.getResources().getDisplayMetrics().density;
        int paddingDp = (int) (paddingPixel * density);
        vi.setPadding(manager.getCommentList().get(position).getDepth() * paddingDp, vi.getPaddingTop(), vi.getPaddingRight(), vi.getPaddingBottom());

        int score = manager.getCommentList().get(position).getScore();
        String points = "0 points";
        if (score < 0) {
            points = score + " points";
        } else if (score > 0) {
            points = "+" + score + " points";
        }
        holder.text5.setText(points);

        final Comment comment = manager.getCommentList().get(position);
        String uName = User.userName(activity);
        if (uName != null && uName.equals(comment.getAuthorName())) {
            holder.text6.setVisibility(View.VISIBLE);
        } else {
            holder.text6.setVisibility(GONE);
        }
        holder.text1.setText(String.valueOf(comment.getAuthorName()));
        holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SearchActivity) activity).hasConnection()) {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("username", comment.getAuthorName());
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Must have internet connection to view user profiles.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String date = comment.getDate();
        try {
            Date dateObject = FormatUtil.RAW_FORMAT.parse(comment.getDate());
            date = FormatUtil.MONTH_DAY_YEAR.format(dateObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.text2.setText(date);
        holder.text3.setText(comment.getText());
        final CommentListAdapter adapter = this;
        holder.text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txtUrl = new EditText(activity);
                if (User.currentToken(activity) != null) {
                    txtUrl.setHint("Type your reply here.");
                    getEditCommentDialog(txtUrl, v, adapter, comment).show();
                } else {
                    Toast.makeText(activity, "Must be logged in to post a comment", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txtUrl = new EditText(activity);
                if (User.currentToken(activity) != null) {
                    txtUrl.setText(holder.text3.getText().toString());
                    final String oldText = holder.text3.getText().toString();
                    getRealEditCommentDialog(txtUrl, v, adapter, comment).show();
                } else {
                    Toast.makeText(activity, "Must be logged in to edit comment.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return vi;

    }

    private AlertDialog getRealEditCommentDialog(final EditText txtUrl, final View view, final CommentListAdapter adapter, final Comment comment) {
        AlertDialog dialog = null;
        dialog = new AlertDialog.Builder(view.getContext())
                .setTitle("Edit")
                .setView(txtUrl)
                .setPositiveButton("Confirm Edit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = txtUrl.getText().toString();
                        if (((CragChatActivity) activity).hasConnection()) {
                            Comment newCom = new Comment(url, comment.getId(), comment.getScore(), comment.getDate(), comment.getDisplayId(),
                                    comment.getParent(), comment.getDepth(), comment.getAuthorName(), comment.getTable());
                            new SendCommentEditTask(activity, adapter, newCom).execute();
                        } else {
                            Toast.makeText(activity, "Unable to post edit (connection error). The app will try again next time it is started with a valid internet connection.", Toast.LENGTH_LONG).show();
                            String store = "COMMENT###" + table + "###" + User.currentToken(activity) + "###" + url + "###" + comment.getDisplayId() + "###" +
                                    comment.getId() + "###" + String.valueOf(comment.getDepth() + 1);
                            LocalDatabase.getInstance(activity).store(activity, store);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog dialog1 = new AlertDialog.Builder(view.getContext())
                                .setMessage("Are you sure you want to cancel this edit?")
                                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        final EditText newt = new EditText(view.getContext());
                                        newt.setText(txtUrl.getText());
                                        getEditCommentDialog(newt, view, adapter, comment).show();
                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                }).create();
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.show();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private AlertDialog getEditCommentDialog(final EditText txtUrl, final View view, final CommentListAdapter adapter, final Comment comment) {
        AlertDialog dialog = null;
        dialog = new AlertDialog.Builder(view.getContext())
                .setTitle("Reply")
                .setView(txtUrl)
                .setPositiveButton("Post Reply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = txtUrl.getText().toString();
                        //  public static String insertComment(String table, String author, String text, String display_id, String parent, String depth) {
                        if (((CragChatActivity) activity).hasConnection()) {
                            new SendCommentTask(activity, adapter, table).execute(table, User.currentToken(activity), url, String.valueOf(comment.getDisplayId()),
                                    String.valueOf(comment.getId()), String.valueOf(comment.getDepth() + 1));

                        } else {
                            Toast.makeText(activity, "Unable to post comment - will try again later", Toast.LENGTH_LONG).show();
                            String store = "COMMENT###" + table + "###" + User.currentToken(activity) + "###" + url + "###" + comment.getDisplayId() + "###" +
                                    comment.getId() + "###" + String.valueOf(comment.getDepth() + 1);
                            LocalDatabase.getInstance(activity).store(activity, store);

                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlertDialog dialog1 = new AlertDialog.Builder(view.getContext())
                                .setMessage("Are you sure you want to cancel this comment?")
                                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        final EditText newt = new EditText(view.getContext());
                                        newt.setText(txtUrl.getText());
                                        getEditCommentDialog(newt, view, adapter, comment).show();
                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                }).create();
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.show();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
