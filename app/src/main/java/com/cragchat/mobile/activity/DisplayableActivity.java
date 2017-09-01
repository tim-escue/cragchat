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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route_activity, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = getDrawerLayout().isDrawerOpen(Gravity.START);
        menu.findItem(R.id.search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
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
        } else if (item.getItemId() == R.id.search) {
            if (getDrawerLayout().isDrawerOpen(Gravity.START)) {
                getDrawerLayout().closeDrawer(Gravity.START);
            } else {
                getDrawerLayout().openDrawer(Gravity.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract Displayable getDisplayable();

    public void addImage(View v) {
        if (User.currentToken(this) != null) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        } else {
            //Log.d("RouteActivity", "Must be logged in to add an image");
            DialogFragment df = NotificationDialog.newInstance("Must be logged in to add an image");
            df.show(getFragmentManager(), "dialog");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Log.d("RouteActivity", "No data");
                return;
            }
            try {
                //System.out.println("INTENT RECEIVED");
                if (hasConnection()) {
                    Intent editImage = new Intent(this, EditImageActivity.class);
                    editImage.putExtra("image_uri", data.getData().toString());
                    editImage.putExtra("displayable_id", getDisplayable().getId());
                    startActivity(editImage);
                    // new SendImageTask(this, User.currentToken(this), data.getData(), route.getId(), "nocap", imageFragment).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No data connection - storing comment to post it later.", Toast.LENGTH_LONG).show();
                    LocalDatabase.getInstance(this).store(this, "IMAGE###" + RemoteDatabase.getPath(this, data.getData()) + "###" + User.currentToken(this) + "###" + getDisplayable().getId() + "###" + "nocap");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }
}
