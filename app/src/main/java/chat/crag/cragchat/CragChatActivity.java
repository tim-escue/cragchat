package chat.crag.cragchat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import chat.crag.cragchat.descriptor.Area;
import chat.crag.cragchat.descriptor.Displayable;
import chat.crag.cragchat.descriptor.Route;
import chat.crag.cragchat.sql.CheckForRouteUpdateTask;
import chat.crag.cragchat.sql.LocalDatabase;

public class CragChatActivity extends AppCompatActivity {

    public static final String DATA_STRING = "DATA_STRING";
    public static long lastConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastConnection = 0;
       // registerReceiver(new Receiver(this),
       //         new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void launch(Displayable r) {
        launch(r, 0);
    }

    public void launch(Displayable r, int tab) {
        Class c;
        String encoded;
        if (r instanceof Route) {
            c = RouteActivity.class;
            encoded = r.toString();
        } else {
            c = AreaActivity.class;
            encoded = Displayable.encodeAsString((Area)r);
        }
        Intent intent = new Intent(this, c);
        intent.putExtra("TAB", tab);
        intent.putExtra(DATA_STRING, encoded);
        startActivity(intent);
    }

    public void openDisplayable(View v) {
        TextView view = (TextView) v;
        String str = view.getText().toString();
        if (str.startsWith("->")) {
            str = str.substring(2);
        }
        Displayable target = LocalDatabase.getInstance(this).findExact(str);
        if (hasConnection()) {
            //Log.d("RemoteDatabase", "Connected to internet - checking revision");
            new CheckForRouteUpdateTask(target, this).execute();
        }
        launch(target);
    }


    public boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


}
