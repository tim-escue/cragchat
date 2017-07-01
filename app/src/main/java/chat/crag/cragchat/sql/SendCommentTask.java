package chat.crag.cragchat.sql;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import chat.crag.cragchat.adapters.CommentListAdapter;
import chat.crag.cragchat.comments.Comment;
import chat.crag.cragchat.remote.RemoteDatabase;
import chat.crag.cragchat.user.User;

public class SendCommentTask extends AsyncTask<String, Integer, String> {

    private Activity activity;
    private CommentListAdapter adapter;
    private String[] params;
    private String table;

    public SendCommentTask(Activity activity, CommentListAdapter adapter, String table) {
        this.adapter = adapter;
        this.activity = activity;
        this.table = table;
    }

    protected String doInBackground(String... params) {
        this.params = params;
        return RemoteDatabase.insertComment(table, params[1], params[2].replaceAll("\n", "<!!!>"), params[3], params[4], params[5]);
    }

    protected void onPostExecute(String feed) {
        //if (feed != null)
       //Log.d("SendComment", feed);
        //else Log.d("SendComment", "was null");
        if (feed != null && feed.contains("success")) {
                String[] res = feed.split("#");
                Comment com = new Comment(params[2], Integer.valueOf(res[1]), 0, res[2], Integer.valueOf(params[3]), Integer.valueOf(params[4]), Integer.valueOf(params[5]), res[3].trim(), table);
                if (adapter != null) {
                    adapter.addComment(com);
                    adapter.notifyDataSetChanged();
                }
                LocalDatabase.getInstance(activity).insert(com);
                Toast.makeText(activity, "Comment posted", Toast.LENGTH_LONG).show();
        } else {
            System.out.println("RESLINE:" + feed);
            Toast.makeText(activity,"Unable to add comment - will try again later", Toast.LENGTH_LONG).show();
            String store = "COMMENT###" +table+ "###" + User.currentToken(activity) + "###" + params[2].replaceAll("\n", "<!!!>") + "###" + params[3] + "###" +
                    params[4] + "###" + params[5];
            LocalDatabase.getInstance(activity).store(activity, store);

        }
    }
}
