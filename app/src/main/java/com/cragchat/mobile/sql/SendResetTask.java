package com.cragchat.mobile.sql;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import com.cragchat.mobile.remote.RemoteDatabase;

import java.util.List;


public class SendResetTask extends AsyncTask<Void, Integer, List<String>> {

    private long cur;
    private String email;
    private Activity act;

    public SendResetTask(Activity act, String email) {
        this.act = act;
        cur = System.currentTimeMillis();
        this.email = email;
    }

    protected List<String> doInBackground(Void... urls) {
        return RemoteDatabase.requestReset(email);
    }

    protected void onPostExecute(List<String> feed) {
        for (String i : feed) {
            if (i.contains("request sent")) {
                Toast.makeText(act, "Reset request sent", Toast.LENGTH_LONG).show();
            }
        }
    }
}