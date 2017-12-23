package com.cragchat.mobile.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetImageTask extends AsyncTask<String, Void, Bitmap> {

    private TaskCallback<Bitmap> callback;
    private String url;

    public GetImageTask(String url, TaskCallback<Bitmap> callback) {
        this.callback = callback;
        this.url = url;
    }

    protected Bitmap doInBackground(String... args) {

        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        Bitmap mIcon11 = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null && response.isSuccessful()) {
            try {
                mIcon11 = BitmapFactory.decodeStream(response.body().byteStream());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        callback.onResult(result);
    }
}