package com.cragchat.mobile.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.fragments.NotificationDialog;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NavigableActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addContent(R.layout.activity_main);

        handlePermission();
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
                LocalDatabase.getInstance(this).updateAll(this);
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
                    fragment.show(getFragmentManager(), "dialog");
                }
            }
        }


    }
}
