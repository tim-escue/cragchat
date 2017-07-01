package chat.crag.cragchat.sql;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import chat.crag.cragchat.remote.RemoteDatabase;

import java.util.List;


public class SendPending extends AsyncTask<Void, Integer, Void> {

    private long cur;
    private List<String> pending;
    private Activity act;

    public SendPending(Activity act, List<String> pending) {
        this.act = act;
        cur = System.currentTimeMillis();
        this.pending = pending;
    }

    protected Void doInBackground(Void... urls) {
        RemoteDatabase.sendPending(act,pending);

        return null;
    }

    protected void onPostExecute(List<String> feed) {
        Toast.makeText(act, "Sent all pending submissions", Toast.LENGTH_LONG).show();
    }
}