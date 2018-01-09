package com.cragchat.mobile.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;

/**
 * Created by timde on 12/30/2017.
 */

public class PermissionUtil {

    public static int REQUEST_CODE = 0;

    public static void requestPermission(final Activity activity, final String permission, String explanation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(explanation);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}
