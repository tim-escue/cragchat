package chat.crag.cragchat.sql;

import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;
import chat.crag.cragchat.CragChatActivity;
import chat.crag.cragchat.R;
import chat.crag.cragchat.adapters.SearchResultListAdapter;
import chat.crag.cragchat.descriptor.Displayable;

public class SearchQueryTask extends AsyncTask<String, Integer, Displayable[]> {

    private CragChatActivity context;

    public SearchQueryTask(CragChatActivity context) {
        this.context = context;
    }

    private String search;

    protected Displayable[] doInBackground(String... urls) {
        search = urls[0];
        return LocalDatabase.getInstance(context).findAllWithName(search);
    }

    protected void onPostExecute(Displayable[] feed) {
        ListView lv = (ListView) context.findViewById(R.id.listView);
        if (feed.length > 0) {
            SearchResultListAdapter adapter = new SearchResultListAdapter(context, feed);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(adapter);
            adapter.notifyDataSetChanged();
        } else {
            TextView empty = (TextView) context.findViewById(R.id.list_empty);
            empty.setText("No results found for \"" + search +"\"\n\n" +
                    "Try another name");
            lv.setEmptyView(empty);
        }
    }
}