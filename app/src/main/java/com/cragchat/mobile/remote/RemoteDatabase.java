package com.cragchat.mobile.remote;


import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.sql.SendCommentEditTask;
import com.cragchat.mobile.sql.SendCommentTask;
import com.cragchat.mobile.sql.SendImageTask;
import com.cragchat.mobile.sql.SendRatingTask;
import com.cragchat.mobile.user.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoteDatabase {

    private static final String URL_ROOT = "ec2-52-34-138-217.us-west-2.compute.amazonaws.com";

    public static void main(String[] args) {
        try {
            // insertComment("BETA", "0", "Steep route on great holds", "73", "-1", "0");
            // insertRoute(9999+"","poopbutt", "TRADBRUH", "13", "5.18b");
           /* try {
                BufferedReader in = new BufferedReader(new FileReader("/home/tim/Desktop/pg/Android/CragChat/app/src/main/res/raw/walls"));
                String ln;
                insertArea("-1", "Ozone");
                while ((ln = in.readLine()) != null) {
                    String[] er = ln.split("#");
                    insertArea("1", er[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            BufferedReader in = new BufferedReader(new FileReader("/home/tim/Desktop/pg/Android/CragChat/app/src/main/res/raw/routes"));
            String ln;
            while ((ln = in.readLine()) != null) {
                String[] er = ln.split("#");
                String[] ar = {er[0], er[3],"1", er[4]};
                String n = ar[0];
                for (int i = 1; i < ar.length; i++) {
                    n += "#" + ar[i];

                }
                switch (er[2]) {
                    case "Shield Wall": insertRoute("2", er[0], er[3], "1", er[4]);
                        break;
                    case "Heaven's Wall": insertRoute("3", er[0], er[3], "1", er[4]);break;
                    case "New School Wall": insertRoute("4", er[0], er[3], "1", er[4]);break;
                    case "Old Tree Wall": insertRoute("5", er[0], er[3], "1", er[4]);break;
                    case "Sport School Wall": insertRoute("6", er[0], er[3], "1", er[4]);break;
                    case "Old School Wall": insertRoute("7", er[0], er[3], "1", er[4]);break;

                    case "Snake Wall": insertRoute("8", er[0], er[3], "1", er[4]);break;

                    case "Shire Wall": insertRoute("9", er[0], er[3], "1", er[4]);break;
                    case "Middle Earth": insertRoute("11", er[0], er[3], "1", er[4]);break;

                    case "Gold Wall": insertRoute("10", er[0], er[3], "1", er[4]);break;
                    case "Mordor": insertRoute("12", er[0], er[3], "1", er[4]);break;

                    default: insertRoute("2", er[0], er[3], "1", er[4]);
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void sendPending(Activity activity, List<String> pending) {
        for (String i : pending) {
            if (i.startsWith("JSON=")) {
                String json = i.substring(5);
                try {
                    new SendCommentEditTask(activity, null, Comment.decode(new JSONObject(json))).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String[] args = i.split("###");
            if (i.startsWith("COMMENT")) {
                new SendCommentTask(activity, null, args[1]).execute(args[1], args[2], args[3], args[4], args[5], args[6]);
            }
            if (i.startsWith("IMAGE")) {
                //Log.d("IMAGE", i);
                new SendImageTask((CragChatActivity) activity, args[2], Uri.fromFile(new File(args[1])), Integer.parseInt(args[3]), args[4], null).execute();
            }
            if (i.startsWith("RATING")) {
                new SendRatingTask((CragChatActivity) activity, Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), args[7], false).execute();
            }
        }
    }

    public static String[] fetchIds() {
        List<String> list = new LinkedList<>();
        try {
            URI uri = new URI(
                    "http",
                    URL_ROOT,
                    "/routedb/updateIds.php",
                    null,
                    null);
            String request = uri.toASCIIString();
            //System.out.println("LINK:" + request);

            URL url = new URL(request);
            URLConnection con = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                if (!message.trim().equals(""))
                    list.add(message.trim());
                //System.out.println(":" + message +":");
            }
            in.close();
            return list.size() > 0 ? list.toArray(new String[list.size()]) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> fetchRatings() {
        return fetchRatings(-1);
    }

    public static List<String> getUserData(String name) {
        return httpGet("getUserData.php", "name=" + name);
    }

    public static List<String> getRecentActivity(int id) {
        String build = null;
        if (id != -1) {
            build = "id=" + id;
        }
        return httpGet("getRecentActivity.php", build);
    }

    public static List<String> httpGet(String phpRsc, String param) {
        List<String> list = new LinkedList<>();
        try {
            URI uri = new URI(
                    "http",
                    URL_ROOT,
                    "/routedb/" + phpRsc,
                    param,
                    null);
            String request = uri.toASCIIString();
            //Log.d("REMOTE GET:", request);

            URL url = new URL(request);
            URLConnection con = url.openConnection();

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                list.add(line.replaceAll("<!!!>", "\n"));
            }
            rd.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static List<String> fetchRatings(int id) {
        //Log.d("RemoteServer", "Fetching ratings" + id);
        String build = null;
        if (id != -1) {
            build = "route_id=" + id;
        }
        return httpGet("getRating.php", build);
    }

    public static List<String> fetchSends() {
        return fetchSends(-1);
    }

    public static List<String> fetchSends(int id) {
        //Log.d("RemoteServer", "Fetching ratings" + id);
        String build = null;
        if (id != -1) {
            build = "route_id=" + id;
        }
        return httpGet("getSends.php", build);
    }

    public static Displayable[] query(String item) {
        List<Displayable> list = new LinkedList<>();
        try {
            String build = "NAME=" + item;
            URI uri = new URI(
                    "http",
                    URL_ROOT,
                    "/routedb/query.php",
                    build,
                    null);
            String request = uri.toASCIIString();
            //System.out.println("LINK:" + request + "\n");

            URL url = new URL(request);
            URLConnection con = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                message = message.trim();
                if (message.startsWith("JSON="))
                    list.add(Route.decodeRoute(new JSONObject(message.split("=")[1].trim())));
                else if (message.startsWith("AREA"))
                    list.add(Area.decodeAreaString(message));
                //System.out.println(message.trim());
            }
            in.close();
            return list.size() > 0 ? list.toArray(new Displayable[list.size()]) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getComments() {
        List<String> list = new LinkedList<>();
        try {
            URI uri = new URI(
                    "http",
                    URL_ROOT,
                    "/routedb/getComments.php",
                    null,
                    null);
            String request = uri.toASCIIString();
            //System.out.println("LINK:" + request + "\n");

            URL url = new URL(request);
            URLConnection con = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                message = message.trim();
                if (message.startsWith("COMMENT") || message.startsWith("IMAGE")) {
                    list.add(message.replaceAll("<!!!>", "\n"));
                }
            }
            in.close();
            return list.size() > 0 ? list : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static List<String> editComment(Activity act, Comment comment) {
        comment.setText(comment.getText().replaceAll("\n", "<!!!>"));
        return httpPost("http://" + URL_ROOT + "/routedb/editComment.php", comment.getJSONString(act, true));
    }

    public static List<String> sendPrivacyChange(Activity act, boolean bool) {
        Map<String, String> map = new HashMap<>();
        map.put("privacy", String.valueOf(bool));
        map.put("userToken", User.currentToken(act));
        JSONObject obj = new JSONObject(map);
        return httpPost("http://" + URL_ROOT + "/routedb/changePrivacy.php", obj.toString());
    }

    public static List<String> httpPost(String urlString, String jsonString) {
        try {
            // open a connection to the site
            URL url = new URL(urlString);
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("JSON=" + jsonString);


            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            List<String> list = new ArrayList<>();
            String line;
            while ((line = rd.readLine()) != null) {
                list.add(line.trim());
            }
            rd.close();
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getComments(int id) {
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/getComments.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("id=" + id);


            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            List<String> list = new ArrayList<>();
            String line;
            while ((line = rd.readLine()) != null) {
                list.add(line.trim().replaceAll("<!!!>", "\n"));
            }
            rd.close();
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void insertArea(String parentId, String name) {
        try {
            String build = "PARENT_ID=" + parentId + "&NAME=" + name;

            URI uri = new URI(
                    "http",
                    URL_ROOT,
                    "/routedb/insertArea.php",
                    build,
                    null);
            String request = uri.toASCIIString();
            //System.out.println("LINK:" + request + "\n");

            URL url = new URL(request);
            URLConnection con = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String message = in.readLine();
            System.err.print(message.trim());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertRoute(String parentid, String name, String routeType, String pitches, String YDS) {
        try {
            String build = "PARENT_ID=" + parentid + "&NAME=" + name + "&ROUTE_TYPE=" + routeType +
                    "&PITCHES=" + pitches + "&YDS=" + YDS;

            URI uri = new URI(
                    "http",
                    URL_ROOT,
                    "/routedb/insertRoute.php",
                    build,
                    null);
            String request = uri.toASCIIString();
            //System.out.println("LINK:" + request + "\n");

            URL url = new URL(request);
            URLConnection con = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String message = in.readLine();
            System.err.print(message.trim());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String authenticate(String username, String password) {
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/authenticate.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("username=" + username);
            ps.print("&password=" + password);


            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.trim().startsWith("Token:")) {
                    rd.close();
                    return line.trim();
                }
            }
            rd.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> requestReset(String email) {
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/requestReset.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("email=" + email);


            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            List<String> list = new ArrayList<>();
            String line;
            while ((line = rd.readLine()) != null) {
                list.add(line.trim());
            }
            rd.close();
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> register(String username, String password, String email) {
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/register.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("username=" + username);
            ps.print("&password=" + password);
            ps.print("&email=" + email);


            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            List<String> list = new ArrayList<>();
            String line;
            while ((line = rd.readLine()) != null) {
                list.add(line.trim());
                //Log.d("AUTH", line);
            }
            rd.close();
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> vote(Activity act, int commentId, boolean up) {
        int score = -1;
        if (up) {
            score = 1;
        }
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/vote.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("comment_id=" + commentId);
            ps.print("&score=" + score);
            ps.print("&token=" + User.currentToken(act));


            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            List<String> list = new ArrayList<>();
            String line;
            while ((line = rd.readLine()) != null) {
                list.add(line.trim());
                //Log.d("Comment score", line);
            }
            rd.close();
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> tagLocation(String displayId, String lat, String lon) {
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/tagLocation.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("ID=" + displayId);
            ps.print("&LAT=" + lat);
            ps.print("&LON=" + lon);


            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            List<String> list = new ArrayList<>();
            String line;
            while ((line = rd.readLine()) != null) {
                list.add(line.trim());
            }
            rd.close();
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int currentRevision(int id) {
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/getRevision.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("ID=" + id);


            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = rd.readLine();
            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static List<String> rate(int routeId, int yds, int stars, String userToken) {
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/rateClimb.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("id=" + routeId);
            ps.print("&yds=" + yds);
            ps.print("&stars=" + stars);
            ps.print("&userToken=" + userToken);
            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

            List<String> list = new LinkedList<>();
            String line;
            while ((line = rd.readLine()) != null) {
                list.add(line.trim());
            }
            rd.close();
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> submitSend(int routeId, String userToken, int pitches, int attempts, String style, String sendType) {
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/addSend.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("route_id=" + routeId);
            ps.print("&pitches=" + pitches);
            ps.print("&attempts=" + attempts);
            ps.print("&style=" + style);
            ps.print("&sendType=" + sendType);
            ps.print("&userToken=" + userToken);
            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

            List<String> list = new LinkedList<>();
            String line;
            while ((line = rd.readLine()) != null) {
                Log.d("SEND", line);
                list.add(line.trim());
            }
            rd.close();
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> uploadFile(Context con, Uri uri, String userToken, int displayId, String caption) {
        try {
            MultipartUtility utility = new MultipartUtility("http://" + URL_ROOT + "/routedb/insertThis.php", "UTF-8");

            String filePath = getPath(con, uri);

            int cutOff = filePath.lastIndexOf("/");
            int end = filePath.lastIndexOf(".");

            String name;
            if (end != -1) {
                name = filePath.substring(cutOff != -1 ? cutOff + 1 : 0, end);
            } else {
                name = filePath.substring(cutOff != -1 ? cutOff + 1 : 0);
            }
            name = name.trim() + ".jpg";
            //System.out.println("UPLOADING#" + name + "#");
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(con.getContentResolver(), uri);
            File file = File.createTempFile(name, null, con.getCacheDir());
            FileOutputStream out = null;
            int size = bitmap.getByteCount();
            double percentage = 100;
            //System.out.println("size:" + size);
            Log.e("COMPRESSING", "SIZE IS: " + size);
            if (size > 10000000) {
                int div = size / 10000000;
                //System.out.println("DIV:" + div);
                percentage = (double) 100 / (div);
            }
            System.out.println("percentage:" + percentage);
            try {
                out = new FileOutputStream(file);
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
            }
            bitmap.recycle();
            utility.addFormField("name", name);
            utility.addFormField("author", userToken);
            utility.addFormField("displayId", displayId + "");
            utility.addFormField("caption", caption);
            utility.addFilePart("file", file);
            List<String> list = new LinkedList<>();
            list.addAll(utility.finish());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static String insertComment(String table, String author, String text, String display_id, String parent, String depth) {
        try {
            // open a connection to the site
            URL url = new URL("http://" + URL_ROOT + "/routedb/insertComment.php");
            URLConnection con = url.openConnection();
            // activate the output
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            // send your parameters to your site
            ps.print("BLET=" + table);
            ps.print("&author=" + author);
            ps.print("&text=" + text);
            ps.print("&display_id=" + display_id);
            ps.print("&parent_id=" + parent);
            ps.print("&depth=" + depth);


            // close the printBufferedReader rd = new BufferedReader(
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                //System.out.println(line);
                if (line.contains("success")) {
                    return line;
                }
            }
            rd.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class MultipartUtility {
        private final String boundary;
        private static final String LINE_FEED = "\r\n";
        private HttpURLConnection httpConn;
        private String charset;
        private OutputStream outputStream;
        private PrintWriter writer;

        /**
         * This constructor initializes a new HTTP POST request with content type
         * is set to multipart/form-data
         *
         * @param requestURL
         * @param charset
         * @throws IOException
         */
        public MultipartUtility(String requestURL, String charset)
                throws IOException {
            this.charset = charset;

            // creates a unique boundary based on time stamp
            boundary = "===" + System.currentTimeMillis() + "===";

            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
            httpConn.setRequestProperty("Test", "Bonjour");
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                    true);
        }

        /**
         * Adds a form field to the request
         *
         * @param name  field name
         * @param value field value
         */
        public void addFormField(String name, String value) {
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                    .append(LINE_FEED);
            writer.append("Content-Type: text/plain; charset=" + charset).append(
                    LINE_FEED);
            writer.append(LINE_FEED);
            writer.append(value).append(LINE_FEED);
            writer.flush();
        }

        /**
         * Adds a upload file section to the request
         *
         * @param fieldName  name attribute in <input type="file" name="..." />
         * @param uploadFile a File to be uploaded
         * @throws IOException
         */
        public void addFilePart(String fieldName, File uploadFile)
                throws IOException {
            String fileName = uploadFile.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append(
                    "Content-Disposition: form-data; name=\"" + fieldName
                            + "\"; filename=\"" + fileName + "\"")
                    .append(LINE_FEED);
            writer.append(
                    "Content-Type: image/jpeg")
                    .append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();
        }

        /**
         * Adds a header field to the request.
         *
         * @param name  - name of the header field
         * @param value - value of the header field
         */
        public void addHeaderField(String name, String value) {
            writer.append(name + ": " + value).append(LINE_FEED);
            writer.flush();
        }

        /**
         * Completes the request and receives response from the server.
         *
         * @return a list of Strings as response in case the server returned
         * status OK, otherwise an exception is thrown.
         * @throws IOException
         */
        public List<String> finish() throws IOException {
            List<String> response = new ArrayList<String>();

            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        httpConn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.add(line);
                }
                reader.close();
                httpConn.disconnect();
            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }

            return response;
        }
    }
}
