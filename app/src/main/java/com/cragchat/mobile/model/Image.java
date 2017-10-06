package com.cragchat.mobile.model;

import android.os.Environment;

import org.json.JSONObject;

import java.io.File;

public class Image implements Datable {

    public static final int COLUMN_DISPLAY_ID = 0;
    public static final int COLUMN_CAPTION = 1;
    public static final int COLUMN_AUTHOR = 2;
    public static final int COLUMN_NAME = 3;
    public static final int COLUMN_DATE = 4;
    private static File storageDir;

    private int displayId;
    private String author;
    private String caption;
    private String name;
    private String date;

    public Image(int displayId, String caption, String author, String name, String date) {
        this.displayId = displayId;
        this.author = author;
        this.caption = caption;
        this.name = name;
        this.date = date;
    }

    public static Image decode(JSONObject json) {
        try {
            return new Image(json.getInt("displayId"), json.getString("caption"), json.getString("author"), json.getString("name"), json.getString("date"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getDisplayId() {
        return displayId;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getCaption() {
        return caption;
    }

    public String getName() {
        return name;
    }

    public static File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        if (storageDir == null) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
            try {
                if (storageDir.exists() && !storageDir.isDirectory()) {
                    storageDir.delete();
                }
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return storageDir;
    }

}
