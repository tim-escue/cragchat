package com.cragchat.mobile.sql;

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

    private CragChatActivity activity;
    private String caption;
    private int parentId;
    private long cur;
    private Uri uri;
    private ImageFragment frag;
    private String token;

    public SendImageTask(CragChatActivity activity, String token, Uri uri, int parentId, String caption, ImageFragment frag) {
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
                        /*File f = new File(path+"/" + image.getName());
                        if (!f.exists()) {
                            f.createNewFile();
                        }
                        FileOutputStream out = null;
                        int size = bitmap.getByteCount();
                        double percentage = 100;
                        //System.out.println("size:" + size);
                        Log.e("COMPRESSING", "SIZE IS: " + size);
                        if (size > 10000000) {
                            int div = size / 10000000;
                            Log.e("COMPRESSING", "div: " + size);
                            percentage = (double) 100 / (div);
                        }
                        System.out.println("percentage:"  + percentage);
                        try {
                            out = new FileOutputStream(f);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, (int) percentage, out); // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }*/

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
