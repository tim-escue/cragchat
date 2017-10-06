package com.cragchat.mobile.sql;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cragchat.mobile.R;
import com.cragchat.mobile.model.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tim on 8/5/16.
 */
public class GrabImageTask extends AsyncTask<Void, Integer, String> {

    private LocalDatabase db;
    private long cur;
    private Image image;
    private ImageView view;
    String dir;
    private boolean thumb;
    private Context context;

    public GrabImageTask(Context context, LocalDatabase db, Image image, ImageView view, boolean thumb) {
        this.db = db;
        cur = System.currentTimeMillis();
        this.image = image;
        this.view = view;
        dir = Image.getAlbumStorageDir("routedb").getPath();
        this.thumb = thumb;
        this.context = context;

    }

    protected String doInBackground(Void... urls) {
        //System.out.println("TRYING IMAGE FOR: " + image.getName());
        String res = "Successful image download for " + image.getName();
        try {
            URL url = new URL("http://ec2-52-34-138-217.us-west-2.compute.amazonaws.com/routeimgs/" + image.getName());
            URLConnection conn = url.openConnection();
            InputStream stream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            stream.close();

            //System.out.println("PATHSAVE: " + dir);
            File save = new File(dir + "/" + image.getName());
            save.createNewFile();
            FileOutputStream out = new FileOutputStream(save);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            //System.out.print("Saved main img:" + save.getPath());

            Bitmap thumb = ThumbnailUtils.extractThumbnail(bitmap, 96, 96);
            File saveThumb = new File(dir + "/thumb_" + image.getName());
            saveThumb.createNewFile();
            out = new FileOutputStream(saveThumb);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            //System.out.print(" Saved thumb img:" + saveThumb.getPath());
            bitmap.recycle();
            thumb.recycle();

        } catch (Exception e) {
            res = "failed for " + image.getName();
            e.printStackTrace();
        }
        return res;
    }


    protected void onPostExecute(String feed) {
        if (feed != null) {
            //Log.d("Task", feed);
        }
        if (view != null) {
            Log.d("imgview", "loading " + image.getName());
            Glide.with(context).load(new File(dir + (thumb ? "/thumb_" + image.getName() : image.getName()))).into(view);
            View progress = ((ViewGroup) view.getParent()).findViewById(R.id.progress_image_load);
            if (progress != null) {
                progress.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}