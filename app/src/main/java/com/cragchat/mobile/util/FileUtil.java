package com.cragchat.mobile.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by timde on 11/10/2017.
 */

public class FileUtil {

    public static File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("FileUtil", "Directory not created");
        }
        return file;
    }

}
