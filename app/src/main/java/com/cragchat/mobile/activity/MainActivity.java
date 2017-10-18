package com.cragchat.mobile.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.database.Database;
import com.cragchat.mobile.fragments.NotificationDialog;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.search.NavigableActivity;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.networkapi.CragChatApi;
import com.cragchat.networkapi.NetworkApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends NavigableActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (!Authentication.isLoggedIn(this)) {
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
        }*/
        /*Authentication.login(this, "timsqdev@gmail.com", "88k88k88k", new AuthenticationCallback() {
            @Override
            public void onAuthenticateSuccess(AuthenticatedUser user) {
                Log.d("AUTHENTICATION", "SUCCESS");
            }

            @Override
            public void onAuthenticateFailed() {
                Log.d("AUTHENTICATION", "FAILED");
            }
        });*/


       /* Area area = Database.getInstance().getArea("Ozone");
        NetworkApi.addArea("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OWRlNzMzYTZkYjE4OTI3YmFmYTJhODIifQ.Ib_REb18GU3KXdc5eQYhgppJFald4bs7NK0lXZjpzLg",
        area);
        for (Area i : area.getSubAreas()) {
            NetworkApi.addArea("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OWRlNzMzYTZkYjE4OTI3YmFmYTJhODIifQ.Ib_REb18GU3KXdc5eQYhgppJFald4bs7NK0lXZjpzLg",
                    i);
            
        }*/
        /*
        List<? extends Area> areas = Database.getInstance().getAllAreas();
        for (Area i : areas) {
            System.out.println("Area:" + i.getKey());
        }*/

        NetworkApi.getInstance().getObject(CragChatApi.TYPE_AREA, null, "all")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody body) throws Exception {
                                   long millis = System.currentTimeMillis();
                                   Database.getInstance().updateBatch(body.string());
                                   long result = System.currentTimeMillis() - millis;
                                   System.out.println("RESULT WAS:" + result);
                               }
                           }
                );

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
