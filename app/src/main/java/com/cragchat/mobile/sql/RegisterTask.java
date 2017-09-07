package com.cragchat.mobile.sql;

import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.cragchat.mobile.activity.LoginActivity;
import com.cragchat.mobile.fragments.NotificationDialog;
import com.cragchat.mobile.remote.RemoteDatabase;

import java.util.List;

public class RegisterTask extends AsyncTask<Void, Integer, List<String>> {


    private long cur;
    private String username;
    private String password;
    private String email;
    private AppCompatActivity con;

    public RegisterTask(AppCompatActivity con, String username, String password, String email) {
        cur = System.currentTimeMillis();
        this.username = username;
        this.email = email;
        this.password = password;
        this.con = con;
    }

    protected List<String> doInBackground(Void... voids) {
        return RemoteDatabase.register(username, password, email);
    }

    protected void onPostExecute(List<String> feed) {
        if (feed != null && feed.size() > 0) {
            String result = feed.get(0);
            if (result.contains("already in use")) {
                DialogFragment df = NotificationDialog.newInstance("Username has already been taken.\nPlease" +
                        "try again with new username.");
                df.show(con.getSupportFragmentManager(), "dialog");
            } else if (result.contains("Registration successful")) {
                DialogFragment df = NotificationDialog.newInstance("Registration successsful!\n Please login.", LoginActivity.class);
                df.show(con.getSupportFragmentManager(), "dialog");
            }
        }
    }
}
