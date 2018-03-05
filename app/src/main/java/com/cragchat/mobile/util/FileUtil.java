package com.cragchat.mobile.util;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cragchat.mobile.GlideApp;
import com.cragchat.mobile.GlideRequest;
import com.cragchat.mobile.GlideRequests;

import java.io.File;

public class FileUtil {

    public static final File ALBUM = FileUtil.getAlbumStorageDir("routedb");

    public static File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("FileUtil", "Directory already exists.");
        }
        return file;
    }

    public static String getLocalImagePath(String imageFilename) {
        return ALBUM.getPath() + "/" + imageFilename;
    }

    public static String getRemoteImagePath(String imageFilename) {
        return "http://ec2-54-148-84-77.us-west-2.compute.amazonaws.com/static/"
                + imageFilename;
    }

    public static void loadIntoImageView(String imageFilename, final ImageView imageView, @Nullable final ProgressBar progressBar) {
        GlideRequests requests = GlideApp.with(imageView);
        GlideRequest<Drawable> drawableRequest;
        File localImage = new File(getLocalImagePath(imageFilename));
        if (localImage.exists()) {
            drawableRequest = requests.load(localImage).diskCacheStrategy(DiskCacheStrategy.NONE);
        } else {
            drawableRequest = requests.load(getRemoteImagePath(imageFilename));
        }
        if (progressBar != null) {
            drawableRequest = drawableRequest.listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(imageView.getContext(), "Could not load image", Toast.LENGTH_SHORT).show();
                    e.logRootCauses("GLIDE");

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            });
        }
        drawableRequest.into(imageView);

    }

}
