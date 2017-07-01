package chat.crag.cragchat.sql;

import android.os.AsyncTask;
import android.util.Log;
import chat.crag.cragchat.remote.RemoteDatabase;

import java.util.List;



public class TagLocationTask extends AsyncTask<String, Integer, List<String>> {

    private LocalDatabase db;
    private long cur;

    public TagLocationTask(LocalDatabase db) {
        this.db = db;
        cur = System.currentTimeMillis();;
    }

    protected List<String> doInBackground(String... urls) {
        return RemoteDatabase.tagLocation(urls[0], urls[1], urls[2]);
    }

    protected void onPostExecute(List<String> feed) {
        for (String i : feed) {
            //Log.d("TagLocationTask", i);
        }
    }
}