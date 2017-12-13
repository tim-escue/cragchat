package com.cragchat.mobile.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.authentication.AuthenticationCallback;
import com.cragchat.mobile.fragments.NotificationDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NavigableActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addContent(R.layout.activity_main);

        setupToolBar();

        handlePermission();
        if (!Authentication.isLoggedIn(this)) {
            Authentication.login(this, "testaccount3", "testaccount3", new AuthenticationCallback() {
                @Override
                public void onAuthenticateSuccess(AuthenticatedUser user) {
                    System.out.println("Successfull login" + user.getName());
                    setupNavMenu();
                }

                @Override
                public void onAuthenticateFailed(String message) {
                    System.out.println("Login Failed:" + message);
                }
            });
        } else {
            System.out.println("ALready logged in.");
        }
    }

    private void setupToolBar() {
        getSupportActionBar().setTitle("CragChat");
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
                    //  LocalDatabase.getInstance(this).updateAll(this);
                    Fragment frg = null;
                    frg = getSupportFragmentManager().findFragmentByTag("someString1");
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
