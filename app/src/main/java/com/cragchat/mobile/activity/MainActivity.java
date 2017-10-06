package com.cragchat.mobile.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.authentication.AuthenticationCallback;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.database.models.RealmRoute;
import com.cragchat.mobile.fragments.NotificationDialog;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.mobile.sql.LocalDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class MainActivity extends NavigableActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Authentication.isLoggedIn(this)) {
            Log.d("AUTH", "Not Logged in");
            Authentication.login(this, "timsqdev@gmail.com", "88k88k88k", new AuthenticationCallback() {
                @Override
                public void onAuthenticateSuccess(AuthenticatedUser user) {

                }

                @Override
                public void onAuthenticateFailed() {
                    Log.d("AUTHENTICATION", "FAILED");
                }
            });
        }

        addContent(R.layout.activity_main);

        setupToolBar();

        handlePermission();
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        toolbar.setTitle(R.string.crags);
        setSupportActionBar(toolbar);
    }

    public void onClick(View v) {
        openDisplayable(v);
    }

    private void handlePermission() {
        try {

            /*
                Handle Permissions Requesets
             */
            int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            }

            if (hasConnection()) {
                // LocalDatabase.getInstance(this).updateAll(this);
            } else {
                Toast.makeText(this, "Data connection not found - app will run in offline mode.", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        //System.out.println("PERMISSIONS REQ");
        for (int i = 0; i < grantResults.length; i++) {
            if (permissions[i].equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d("Permission", "Granted permission for permission[" + i + "] " + permissions[i]);
                    LocalDatabase.getInstance(this).updateAll(this);
                    Fragment frg = null;
                    frg = getSupportFragmentManager().findFragmentByTag("someTag1");
                    if (frg != null) {
                        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commitAllowingStateLoss();
                    }
                } else {
                    DialogFragment fragment = NotificationDialog.newInstance("You will not be able to view photos without external storage.\n" +
                            "If you would like to allow this feature please restart the app.");
                    fragment.show(getSupportFragmentManager(), "dialog");
                }
            }
        }


    }
}
