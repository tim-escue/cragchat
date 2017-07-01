package chat.crag.cragchat.sql;

import android.os.AsyncTask;
import chat.crag.cragchat.CragChatActivity;
import chat.crag.cragchat.comments.Comment;
import chat.crag.cragchat.descriptor.Displayable;
import chat.crag.cragchat.descriptor.Image;
import chat.crag.cragchat.remote.RemoteDatabase;

import java.util.List;


public class UpdateRouteCommentsImagesTask extends AsyncTask<Void, Integer, List<String>> {

    private LocalDatabase db;
    private int id;
    private CragChatActivity act;
    private Displayable r;

    public UpdateRouteCommentsImagesTask(LocalDatabase db, CragChatActivity act, Displayable r) {
        this.db = db;
        this.id = r.getId();
        this.act = act;
        this.r = r;
    }

    protected List<String> doInBackground(Void... urls) {
        return RemoteDatabase.getComments(id);
    }

    protected void onPostExecute(List<String> allComments) {
        List<Comment> betaComments = db.getCommentsFor(id, "BETA");
        betaComments.addAll(db.getCommentsFor(id, "DISCUSSION"));
        if (allComments != null) {
            for (String i : allComments) {
                String[] args = i.split("###");
                if (i.startsWith("COMMENT")) {
                    Comment com = new Comment(args[6], Integer.parseInt(args[4]),
                            Integer.parseInt(args[3]), args[5], Integer.parseInt(args[7]),
                            Integer.parseInt(args[8]), Integer.parseInt(args[9]), args[10], args[11]);
                    if (!db.containsId(betaComments, Integer.parseInt(args[4]))) {
                        db.insert(com);
                    } else {
                        db.updateComment(com.getScore(), com.getId());
                    }
                } else if (i.startsWith("IMAGE")){
                        db.insert(new Image(Integer.parseInt(args[1]), args[2], args[3], args[4],args[5]));

                }
            }
            act.launch(r);
        }
    }
}