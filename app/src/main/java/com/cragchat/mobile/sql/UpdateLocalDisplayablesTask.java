package com.cragchat.mobile.sql;

import android.os.AsyncTask;

import com.cragchat.mobile.model.Displayable;
import com.cragchat.mobile.model.LegacyArea;
import com.cragchat.mobile.model.LegacyRoute;
import com.cragchat.mobile.remote.RemoteDatabase;

public class UpdateLocalDisplayablesTask extends AsyncTask<Void, Integer, Displayable[]> {

    private LocalDatabase db;
    private long cur;

    public UpdateLocalDisplayablesTask(LocalDatabase db) {
        this.db = db;
        cur = System.currentTimeMillis();
    }

    protected Displayable[] doInBackground(Void... urls) {
        return RemoteDatabase.query("");
    }

    protected void onPostExecute(Displayable[] feed) {
        if (feed != null) {
            for (Displayable a : feed) {
                if (a instanceof LegacyRoute) {
                    db.insert((LegacyRoute) a);
                } else if (a instanceof LegacyArea) {
                    db.insert((LegacyArea) a);
                }
            }
            //Log.d("Task", "Updated displayables with " + feed.length + " insertions made in "  + (System.currentTimeMillis() - cur) + " millis.");
        }
    }
}