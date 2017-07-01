package chat.crag.cragchat.sql;

import android.os.AsyncTask;
import chat.crag.cragchat.remote.RemoteDatabase;

public class UpdateLocalIdsTask extends AsyncTask<Void, Integer, String[]> {

    private LocalDatabase db;
    private long cur;

    public UpdateLocalIdsTask(LocalDatabase db) {
        this.db = db;
        cur = System.currentTimeMillis();;
    }

    protected String[] doInBackground(Void... urls) {
        return RemoteDatabase.fetchIds();
    }

    protected void onPostExecute(String[] feed) {
        if (feed != null) {
            for (String a : feed) {
                String[] broken = a.split("#");
                int[] vals = {Integer.parseInt(broken[0]), Integer.parseInt(broken[1]), Integer.parseInt(broken[2])};
                db.insert(vals);
            }
        }
    }
}