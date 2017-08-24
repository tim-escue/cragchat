package com.cragchat.mobile.sql;

import android.os.AsyncTask;

import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.remote.RemoteDatabase;


public class CheckForRouteUpdateTask extends AsyncTask<Void, Integer, Displayable[]> {

    private long cur;
    private Displayable target;
    private CragChatActivity act;

    public CheckForRouteUpdateTask(Displayable target, CragChatActivity act) {
        cur = System.currentTimeMillis();
        this.target = target;
        this.act = act;
    }

    protected Displayable[] doInBackground(Void... urls) {
        int remRev = RemoteDatabase.currentRevision(target.getId());
        //Log.d("RemoteDatabase", "remRev=" + remRev);
        if (remRev > target.getRevision()) {
            //Log.d("RemoteDatabase", "New revision found for " + target.getName());
            return RemoteDatabase.query(target.getName());
        }
        //Log.d("RemoteDatabase", "Revision is up to date for " + target.getName());
        return null;
    }

    protected void onPostExecute(Displayable[] d) {
        //Log.d("TASK", "UpdateCommentsTask completed.");
        if (d != null && d.length > 0) {
            LocalDatabase.getInstance(act).update(d[0], act);
        }
    }
}