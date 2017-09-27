package com.cragchat.mobile.sql;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.descriptor.Image;
import com.cragchat.mobile.fragments.ImageFragment;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.remote.ResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class SendImageTask extends AsyncTask<String, Integer, List<String>> {

    private Context activity;
    private String caption;
    private int parentId;
    private long cur;
    private Uri uri;
    private ImageFragment frag;
    private String token;

    public SendImageTask(Context activity, String token, Uri uri, int parentId, String caption, ImageFragment frag) {
        this.activity = activity;
        this.caption = caption;
        this.parentId = parentId;
        cur = System.currentTimeMillis();
        this.uri = uri;
        this.token = token;
        this.frag = frag;
    }

    protected List<String> doInBackground(String... params) {
        //Log.d("SendImageTask", "BACKGROUD");
        return RemoteDatabase.uploadFile(activity, uri, token, parentId, caption);
    }

    protected void onPostExecute(List<String> feed) {
        //Log.d("SendImageTask", "FINISHED IN: " + (System.currentTimeMillis() - cur) + " MILLIS");

        if (feed != null) {
            for (String str : feed) {
                //Log.d("SendImageTask", str);
                str = str.trim();
                if (str.startsWith("JSON=")) {
                    //Log.d("IMAGE", "TRYIG JSON");
                    Image image = (Image) ResponseHandler.parseResponse(activity, str);
                    LocalDatabase.getInstance(activity).insert(image);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        String path = Image.getAlbumStorageDir("routedb").getPath();
                        Bitmap thumb = ThumbnailUtils.extractThumbnail(bitmap, 96, 96);
                        File saveThumb = new File(path + "/thumb_" + image.getName());
                        if (!saveThumb.exists()) {
                            saveThumb.createNewFile();
                        }
                        FileOutputStream stream = new FileOutputStream(saveThumb);
                        thumb.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        stream.close();

                        bitmap.recycle();
                        thumb.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (frag != null) {
                        frag.load(frag.getView());
                    }
                    Toast.makeText(activity, "Image upload success!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, "Image failed to upload - will try again later", Toast.LENGTH_LONG).show();
                    LocalDatabase.getInstance(activity).store(activity, "IMAGE###" + RemoteDatabase.getPath(activity, uri) + "###" + token + "###" + parentId + "###" + caption);
                }
            }
        }
    }
}
