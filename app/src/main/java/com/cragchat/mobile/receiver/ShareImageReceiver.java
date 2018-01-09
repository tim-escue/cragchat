package com.cragchat.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.ui.view.activity.EditImageActivity;

/**
 * Created by timde on 11/28/2017.
 */

public class ShareImageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Authentication.isLoggedIn(context)) {
            Uri imageUri = (Uri) intent.getExtras().get(Intent.EXTRA_STREAM);
            Intent editImageIntent = new Intent(context, EditImageActivity.class);
            editImageIntent.putExtra("image_uri", imageUri.toString());
            /*
                Todo:
                    Must provide an entity_id -- Will have to implement a screen asking which
                    route or  area the picture should be associated with.
             */
            context.startActivity(editImageIntent);
        }
    }
}
