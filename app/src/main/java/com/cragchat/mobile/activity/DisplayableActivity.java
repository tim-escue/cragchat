package com.cragchat.mobile.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.fragments.NotificationDialog;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.user.User;

/**
 * Created by timde on 8/31/2017.
 */

public abstract class DisplayableActivity extends NavigableActivity {

    public static final int PICK_IMAGE = 873;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Displayable parent = LocalDatabase.getInstance(this).getParent(getDisplayable());
            if (parent != null) {
                launch(parent);
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract Displayable getDisplayable();


}
