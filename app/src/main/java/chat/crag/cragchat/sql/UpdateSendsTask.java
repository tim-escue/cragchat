package chat.crag.cragchat.sql;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import chat.crag.cragchat.descriptor.Rating;
import chat.crag.cragchat.descriptor.Send;
import chat.crag.cragchat.remote.RemoteDatabase;
import chat.crag.cragchat.remote.ResponseHandler;

import java.util.List;

public class UpdateSendsTask extends AsyncTask<Void, Integer, List<String>> {

    private long cur;
    private Context con;

    public UpdateSendsTask(Context con) {
        cur = System.currentTimeMillis();
        this.con = con;
    }

    protected List<String> doInBackground(Void... urls) {
        return RemoteDatabase.fetchSends();
    }

    protected void onPostExecute(List<String> feed) {
        if (feed != null) {
            for (String a : feed) {
                Send resp = (Send)ResponseHandler.parseResponse(con, a);
                if (resp != null) {
                    LocalDatabase.getInstance(con).insert(resp);
                }
            }
            //System.out.println("Updated Routes and Areas with " + feed.length + " insertions made in "  + (System.currentTimeMillis() - cur) + " millis.");
        }
    }
}