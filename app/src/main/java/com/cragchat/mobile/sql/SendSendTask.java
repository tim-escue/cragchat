package com.cragchat.mobile.sql;

import android.os.AsyncTask;

import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.model.LegacyRoute;
import com.cragchat.mobile.model.Send;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.remote.ResponseHandler;

import java.util.List;

public class SendSendTask extends AsyncTask<Void, Integer, List<String>> {

    private CragChatActivity con;
    private int id;
    private boolean openAfter;
    private String style;
    private int pitches;
    private String token;
    private String sendType;
    private int attempts;


    public SendSendTask(CragChatActivity con, int id, int attempts, int pitches, String style, String sendType, String userToken, boolean openAfter) {
        this.con = con;
        this.id = id;
        this.openAfter = openAfter;
        this.style = style;
        this.pitches = pitches;
        this.token = userToken;
        this.sendType = sendType;
        this.attempts = attempts;
    }

    protected List<String> doInBackground(Void... voids) {
        return RemoteDatabase.submitSend(id, token, pitches, attempts, style, sendType);
    }

    protected void onPostExecute(List<String> feed) {
        if (feed != null) {
            for (String a : feed) {
                Send r = (Send) ResponseHandler.parseResponse(con, a);
                if (r != null) {
                    LocalDatabase.getInstance(con).insert(r);
                }
            }
            if (openAfter) {
                LegacyRoute r = (LegacyRoute) LocalDatabase.getInstance(con).findExact(id);
                con.launch(r, 4);
            }
        }
    }
}