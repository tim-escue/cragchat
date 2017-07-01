package chat.crag.cragchat.sql;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import chat.crag.cragchat.comments.Comment;
import chat.crag.cragchat.remote.RemoteDatabase;
import chat.crag.cragchat.remote.ResponseHandler;
import chat.crag.cragchat.user.User;

import java.util.List;

public class SendPrivacyChangeTask extends AsyncTask<Void, Integer, List<String>> {

    private long cur;
    private boolean privacy;
    private Activity act;

    public SendPrivacyChangeTask(Activity act, boolean privacy) {
        this.act = act;
        cur = System.currentTimeMillis();
        this.privacy = privacy;
    }

    protected List<String> doInBackground(Void... urls) {
        //Log.d("sendprivacychage", "sending");
        return RemoteDatabase.sendPrivacyChange(act, privacy);
    }

    protected void onPostExecute(List<String> feed) {
        for (String i : feed) {
            //Log.d("PRIVACY", "<------->"+i+"<------->");
           if (i.contains("privacy changed")) {
               String[] vals = i.split(" ");
               Toast.makeText(act, "Privacy setting has been changed to " + vals[vals.length-1], Toast.LENGTH_SHORT).show();
               User.setPrivacy(act, vals[vals.length-1]);
           }
        }
    }
}