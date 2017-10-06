package com.cragchat.mobile.database;

import android.content.Context;

/**
 * Created by timde on 9/28/2017.
 */

public class Database {

    private static CragChatDatasource mDatasource;

    public static void init(Context context){
        mDatasource = new RealmDatabase(context);
    }

    public static CragChatDatasource getInstance() {
        return mDatasource;
    }


}
