package com.cragchat.mobile.sql;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.ProfileActivity;
import com.cragchat.mobile.fragments.NotificationDialog;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.user.User;

public class LoginTask extends AsyncTask<Void, Integer, String> {


    private long cur;
    private String username;
    private String password;
    private AppCompatActivity con;

    public LoginTask(AppCompatActivity con, String username, String password) {
        cur = System.currentTimeMillis();
        this.username = username;
        this.password = password;
        this.con = con;
    }

    protected String doInBackground(Void... voids) {
        return RemoteDatabase.authenticate(username, password);
    }

    protected void onPostExecute(String feed) {
        //Log.d("LoginTask", "LoginTask completed.");
        if (feed != null) {
            String[] args = feed.split(":");
            //System.out.println("Token:" + args[1] + " Username:" + args[2]);
            User.setUser(con, args[1].trim(), args[2].trim(), Boolean.parseBoolean(args[3].trim()));
            //System.out.println("Login took " + (System.currentTimeMillis() - cur) + " millis.");
            Intent reg = new Intent(con, ProfileActivity.class);
            reg.putExtra("username", User.userName(con));
            con.startActivity(reg);
        } else {
            //Log.d("LoginTask", "Username and password combination failed");
            DialogFragment df = NotificationDialog.newInstance("Login failed for username/password combination.");
            User.setUser(con, null, null, false);
            df.show(con.getFragmentManager(), "dialog");
            ((EditText) con.findViewById(R.id.login_password)).setText("");
        }
    }
}
