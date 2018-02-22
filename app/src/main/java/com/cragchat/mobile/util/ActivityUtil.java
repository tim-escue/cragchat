package com.cragchat.mobile.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by timde on 2/5/2018.
 */

public class ActivityUtil {


        public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment fragment, int frameId,
                                                  String tag) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment, tag);
            transaction.commit();
        }

    public static void addFragmentToActivity (@NonNull android.app.FragmentManager fragmentManager,
                                              @NonNull android.app.Fragment fragment, int frameId,
                                              String tag) {
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, tag);
        transaction.commit();
    }


}
