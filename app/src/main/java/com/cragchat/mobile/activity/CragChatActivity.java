package com.cragchat.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.sql.CheckForRouteUpdateTask;
import com.cragchat.mobile.sql.LocalDatabase;

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
        if (v instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) v;
            TextView view = (TextView) layout.findViewById(R.id.text_location);
            Displayable target = LocalDatabase.getInstance(this).findExact(view.getText().toString());
            if (hasConnection()) {
                //Log.d("RemoteDatabase", "Connected to internet - checking revision");
                new CheckForRouteUpdateTask(target, this).execute();
            }
            launch(target);
        }
    }


    public boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


}
