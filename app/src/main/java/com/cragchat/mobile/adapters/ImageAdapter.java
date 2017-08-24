package com.cragchat.mobile.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.cragchat.mobile.R;
import com.cragchat.mobile.descriptor.Image;
import com.cragchat.mobile.sql.GrabImageTask;
import com.cragchat.mobile.sql.LocalDatabase;
import com.bumptech.glide.Glide;

import java.io.File;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private File album;

    public ImageAdapter(Context c, Image[] images) {
        mContext = c;
        album = Image.getAlbumStorageDir("routedb");
        this.images = images;
    }

    public int getCount() {
        return images.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96, mContext.getResources().getDisplayMetrics());
            imageView.setLayoutParams(new GridView.LayoutParams(height, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        File imageFile = new File(album.getPath() + "/" + images[position].getName());
        if (imageFile.exists()) {
            Glide.with(mContext).load(imageFile).into(imageView);
        } else {
            Glide.with(mContext).load(R.drawable.tap_to_load).into(imageView);
        }

      //  imageView.setImageBitmap(bitmaps[position]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(album.getPath() + "/" + images[position].getName());
                if (file.exists()) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    Uri data = Uri.parse("file://" + file.getAbsolutePath());
                    intent.setDataAndType(data, "image/*");
                    Log.d("imagething", data.toString() + "actual path" + file.getAbsolutePath());
                    mContext.startActivity(intent);
                } else {
                    int permissionWriteExternal = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionWriteExternal == PackageManager.PERMISSION_GRANTED) {
                        new GrabImageTask(mContext, LocalDatabase.getInstance(mContext), images[position], imageView, true).execute();
                    } else {
                        Toast.makeText(mContext, "App needs permission to Write To External Storage to load images.", Toast.LENGTH_SHORT).show();
                    }
                    //System.out.println("loading image");
                }
            }
        });
        return imageView;
    }

    // references to our images
    private Image[] images;
}