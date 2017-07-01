package chat.crag.cragchat.sql;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import chat.crag.cragchat.descriptor.Rating;
import chat.crag.cragchat.remote.RemoteDatabase;
import chat.crag.cragchat.remote.ResponseHandler;

import java.util.List;

public class UpdateRatingsTask extends AsyncTask<Void, Integer, List<String>> {

    private long cur;
    private Context con;

    public UpdateRatingsTask(Context con) {
        cur = System.currentTimeMillis();
        this.con = con;
    }

    protected List<String> doInBackground(Void... urls) {
        return RemoteDatabase.fetchRatings();
    }

    protected void onPostExecute(List<String> feed) {
        if (feed != null) {
            for (String a : feed) {
                Rating resp = (Rating)ResponseHandler.parseResponse(con, a);
                //Log.d("UPDATING RATING", a);
                if (resp != null) {
                    LocalDatabase.getInstance(con).insert(resp);
                }
            }
            //System.out.println("Updated Routes and Areas with " + feed.length + " insertions made in "  + (System.currentTimeMillis() - cur) + " millis.");
        }
    }
}