package com.cragchat.mobile.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;

import com.cragchat.mobile.R;
import com.cragchat.mobile.fragments.NotificationDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NavigableActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addContent(R.layout.activity_main);

        handlePermission();
    }

    private void handlePermission() {
        try {

            /*
                Handle Permissions Requesets
             */
            int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<>();
            //if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            //   listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            //}
            if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                //   ActivityCompat.requestPermissions(this,
                //          listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (permissions[i].equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    DialogFragment fragment = NotificationDialog.newInstance("You will not be able to view photos without external storage.\n" +
                            "If you would like to allow this feature please restart the app.");
                    fragment.show(getSupportFragmentManager(), "dialog");
                }
            }
        }


    }
}
