package com.cragchat.mobile.sql;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cragchat.mobile.adapters.CommentListAdapter;
import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.remote.ResponseHandler;

import java.util.List;

public class SendCommentEditTask extends AsyncTask<String, Integer, List<String>> {

    private Activity activity;
    private CommentListAdapter adapter;
    private Comment comment;

    public SendCommentEditTask(Activity activity, CommentListAdapter adapter, Comment commet) {
        this.adapter = adapter;
        this.activity = activity;
        this.comment = commet;
    }

    protected List<String> doInBackground(String... params) {
        return RemoteDatabase.editComment(activity, comment);
    }

    protected void onPostExecute(List<String> feed) {
        for (String i : feed) {
            System.out.println("FEED:" + feed);
            Comment com = (Comment) ResponseHandler.parseResponse(activity, i);
            if (com != null) {
                if (adapter != null) {
                    adapter.edit(com);
                    adapter.notifyDataSetChanged();
                }
                LocalDatabase.getInstance(activity).insert(com);
                Toast.makeText(activity, "Comment editted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "Unable to edit comment - will try again later", Toast.LENGTH_LONG).show();
                LocalDatabase.getInstance(activity).store(activity, "JSON=" + comment.getJSONString(activity, true));
            }
        }


    }
}
