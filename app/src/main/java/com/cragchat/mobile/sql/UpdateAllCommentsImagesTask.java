package com.cragchat.mobile.sql;

import android.os.AsyncTask;

import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.descriptor.Image;
import com.cragchat.mobile.remote.RemoteDatabase;

import java.util.List;

public class UpdateAllCommentsImagesTask extends AsyncTask<Void, Integer, List<String>> {

    private LocalDatabase db;
    private long cur;

    public UpdateAllCommentsImagesTask(LocalDatabase db) {
        this.db = db;
        cur = System.currentTimeMillis();
        ;
    }

    protected List<String> doInBackground(Void... urls) {
        return RemoteDatabase.getComments();
    }

    protected void onPostExecute(List<String> feed) {
        //Log.d("TASK", "UpdateCommentsTask completed.");
        if (feed != null) {
            db.deleteComments();
            for (String a : feed) {
                String[] args = a.split("###");
                if (a.trim().startsWith("COMMENT")) {
                    //Log.d("UPDATING COMMENT", a);
                    if (args.length < 11) {
                        continue;
                    }
                    db.insert(new Comment(args[5], Integer.parseInt(args[3]),
                            Integer.parseInt(args[2]), args[4], Integer.parseInt(args[6]),
                            Integer.parseInt(args[7]), Integer.parseInt(args[8]), args[9], args[10]));
                } else if (a.startsWith("IMAGE")) {
                    db.insert(new Image(Integer.parseInt(args[1]), args[2], args[3], args[4], args[5]));
                }
            }
            //System.out.println("Updated all comments in " + (System.currentTimeMillis() - cur) + " millis.");
        }
    }
}