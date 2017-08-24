package com.cragchat.mobile.sql;

import android.os.AsyncTask;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.descriptor.Rating;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.remote.ResponseHandler;

import java.util.List;

public class SendRatingTask extends AsyncTask<Void, Integer, List<String>> {


    private int yds;
    private int stars;
    private CragChatActivity con;
    private int id;
    private boolean openAfter;
    private String style;
    private int pitches;
    private int timeSeconds;
    private String token;
    private String sendType;
    private int attempts;


    public SendRatingTask(CragChatActivity con, int id, int yds, int stars, String userToken, boolean openAfter) {
        this.yds = yds;
        this.stars = stars;
        this.con = con;
        this.id = id;
        this.openAfter = openAfter;
        this.style = style;
        this.pitches = pitches;
        this.timeSeconds = timeSeconds;
        this.token = userToken;
    }

    protected List<String> doInBackground(Void... voids) {
        return RemoteDatabase.rate(id, yds, stars,token);
    }

    protected void onPostExecute(List<String> feed) {
        if (feed != null) {
            for (String a : feed) {
                Rating r = (Rating) ResponseHandler.parseResponse(con, a);
                if (r != null) {
                    LocalDatabase.getInstance(con).insert(r);
                }
            }
            if (openAfter) {
                Route r = (Route)LocalDatabase.getInstance(con).findExact(id);
                con.launch(r, 2);
            }
        }
    }
}