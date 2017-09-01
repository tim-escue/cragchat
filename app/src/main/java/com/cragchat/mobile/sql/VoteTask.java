package com.cragchat.mobile.sql;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.remote.RemoteDatabase;

import java.util.List;

public class VoteTask extends AsyncTask<Void, Integer, String[]> {

    private long cur;
    private Activity con;
    private boolean up;
    private Comment comment;
    private TextView view;

    public VoteTask(Activity con, Comment comment, boolean up, TextView view) {
        cur = System.currentTimeMillis();
        this.con = con;
        this.view = view;
        this.comment = comment;
        this.up = up;
    }

    protected String[] doInBackground(Void... urls) {
        List<String> list = RemoteDatabase.vote(con, comment.getId(), up);
        return list == null ? null : list.toArray(new String[list.size()]);
    }

    protected void onPostExecute(String[] feed) {
        if (feed != null) {
            for (String a : feed) {
                if (a.contains("success")) {
                    int score = Integer.parseInt(a.split(":")[1]);
                    LocalDatabase.getInstance(con).updateComment(score, comment.getId());
                    comment.setScore(score);
                    String points = "0 points";
                    if (score < 0) {
                        points = score + " points";
                    } else if (score > 0) {
                        points = "+" + score + " points";
                    }
                    view.setText(points);
                    //System.out.println("Updated comment score");
                }
            }
            //System.out.println("Updated Routes and Areas with " + feed.length + " insertions made in "  + (System.currentTimeMillis() - cur) + " millis.");
        }
    }
}