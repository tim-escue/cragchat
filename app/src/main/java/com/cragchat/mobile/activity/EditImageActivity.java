package com.cragchat.mobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import com.cragchat.mobile.R;
import com.cragchat.mobile.descriptor.Image;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.remote.RemoteDatabase;
import com.cragchat.mobile.sql.SendImageTask;
import com.cragchat.mobile.user.User;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EditImageActivity extends CragChatActivity {

    private Bitmap image;
    private Bitmap original;
    private boolean addingBolts = false;
    private boolean addingAnchors = false;
    private boolean tracing = false;
    private List<Point> tracePoints;
    private List<Point> bolts;
    private List<Point> anchors;
    private Uri uri;

    private float innerStroke;
    private float outterStroke;
    private Paint strokePaint;
    private String caption;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_image_edit);

        tracePoints = new ArrayList<>(100);
        bolts = new ArrayList<>(30);
        anchors = new ArrayList<>(10);

        final ImageView view = (ImageView) findViewById(R.id.image_edit);
        Bitmap bitmap = null;
        Route r = null;
        this.uri = Uri.parse(getIntent().getStringExtra("image_uri"));
        try {
            r = Route.decodeRoute(new JSONObject(getIntent().getStringExtra("route_json")));
            bitmap = getCorrectlyOrientedImage(this, uri);
            image = bitmap;
            original = bitmap;
            view.setImageBitmap(image);
        } catch (Exception e) {
            e.printStackTrace();
            int nh = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()) );
            image = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
            view.setImageBitmap(image);
        }

        innerStroke = original.getWidth() / 64;
        outterStroke = original.getWidth() / 64;
        strokePaint = new Paint();

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);

        Button rotateButton = (Button) findViewById(R.id.button_rotate);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90.0f);
                image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
                view.setImageBitmap(image);
            }
        });
        final Route rFinal = r;

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float[] coords = getPointerCoords(view, event);
                Point newPoint = new Point((int)coords[0], (int)coords[1]);

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (addingBolts || addingAnchors) {
                        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), addingBolts ? R.drawable.bolt : R.drawable.anchor);

                        Bitmap resultBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);

                        Canvas c = new Canvas(resultBitmap);

                        c.drawBitmap(image, 0, 0, null);

                        Paint p = new Paint();
                        p.setAlpha(255);

                        c.drawBitmap(bitmap2, addingBolts ? (int) coords[0] : (int) (coords[0] - bitmap2.getWidth() / 2), (int) coords[1], p);

                        if (addingBolts)
                            bolts.add(newPoint);
                        else
                            anchors.add(newPoint);

                        image = resultBitmap;
                        view.setImageBitmap(image);
                    }
                }
                if (tracing) {
                    if (tracePoints == null) {
                        tracePoints = new ArrayList<Point>(100);
                    }
                    if (tracePoints.size() == 0) {
                        tracePoints.add(newPoint);
                    } else if (dist(tracePoints.get(tracePoints.size()-1), newPoint) > 12) {
                        Bitmap resultBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas c = new Canvas(resultBitmap);
                        c.drawBitmap(image, 0, 0, null);

                        /*Point old = tracePoints.get(tracePoints.size()-1);

                        strokePaint.setStrokeWidth(outterStroke);
                        strokePaint.setColor(Color.WHITE);
                        c.drawLine((float)old.x, (float)old.y,(float) newPoint.x, (float)newPoint.y, strokePaint);

                        strokePaint.setColor(Color.YELLOW);
                        strokePaint.setStrokeWidth(innerStroke);
                        c.drawLine((float)old.x, (float)old.y,(float) newPoint.x, (float)newPoint.y, strokePaint);
                        */
                        strokePaint.setStrokeWidth(outterStroke);
                        strokePaint.setColor(Color.WHITE);
                        drawPath(c, tracePoints, strokePaint);
                        strokePaint.setColor(Color.YELLOW);
                        strokePaint.setStrokeWidth(innerStroke);
                        drawPath(c, tracePoints, strokePaint);

                        tracePoints.add(newPoint);
                        image = resultBitmap;
                        view.setImageBitmap(image);

                    }
                }
                return true;
            }
        });

        final Button undo = (Button) findViewById(R.id.button_undo_last);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addingBolts && bolts.size() > 0) {
                    bolts.remove(bolts.size() - 1);
                } else if (addingAnchors && anchors.size() > 0) {
                    anchors.remove(anchors.size()- 1);
                } else if (tracing && tracePoints.size() > 0) {
                    tracePoints.remove(tracePoints.size() - 1);
                }


                Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.bolt);

                Bitmap resultBitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas c = new Canvas(resultBitmap);

                c.drawBitmap(original, 0, 0, null);

                if (tracePoints.size() > 1) {
                    /*Point old = tracePoints.get(0);
                    for (int i = 1; i < tracePoints.size(); i++) {
                        Point newPoint = tracePoints.get(i);
                        strokePaint.setColor(Color.WHITE);
                        strokePaint.setStrokeWidth(outterStroke);
                        c.drawLine((float) old.x, (float) old.y, (float) newPoint.x, (float) newPoint.y, strokePaint);

                        strokePaint.setColor(Color.YELLOW);
                        strokePaint.setStrokeWidth(innerStroke);
                        c.drawLine((float) old.x, (float) old.y, (float) newPoint.x, (float) newPoint.y, strokePaint);
                        old = newPoint;
                    }*/
                    drawPath(c, tracePoints, strokePaint);
                }

                strokePaint.setAlpha(255);
                for (Point i : bolts) {
                    c.drawBitmap(bitmap2, i.x, i.y, strokePaint);
                }

                bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.anchor);

                for (Point i : anchors) {
                    c.drawBitmap(bitmap2, i.x - bitmap2.getWidth() / 2, i.y, strokePaint);
                }


                image = resultBitmap;
                ((BitmapDrawable)view.getDrawable()).getBitmap().recycle();
                bitmap2.recycle();
                view.setImageBitmap(image);
                //resultBitmap.recycle();



            }
        });

        final Button addVisual = (Button) findViewById(R.id.button_add_beta);
        final Activity act = this;
        addVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button stopEdditing = (Button) findViewById(R.id.button_stop_editting);
                stopEdditing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout lay = (LinearLayout) findViewById(R.id.layout_edit_options);
                        lay.setVisibility(View.VISIBLE);
                        lay = (LinearLayout) findViewById(R.id.layout_edit_current);
                        lay.setVisibility(View.GONE);
                        lay = (LinearLayout) findViewById(R.id.layout_image_submit);
                        lay.setVisibility(View.VISIBLE);
                        addingBolts = false;
                        addingAnchors = false;
                        tracing = false;
                    }
                });
                    AlertDialog.Builder builder = new AlertDialog.Builder(act);
                    builder.setTitle(R.string.pick_edit_option)
                            .setItems(R.array.array_edit_options, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    TextView tv = (TextView) findViewById(R.id.text_edit_instructions);
                                    String text = "";
                                    switch (which) {
                                        case 0:
                                            addingBolts = true;
                                            text = "Tap to add bolt / placement";
                                            break;
                                        case 1:
                                            text = "Tap to add anchors";
                                            addingAnchors = true;
                                            break;
                                        case 2:
                                            text = "Outline route with your finger";
                                            tracing = true;
                                            break;
                                    }
                                    tv.setText(text);
                                    LinearLayout lay = (LinearLayout) findViewById(R.id.layout_edit_options);
                                    lay.setVisibility(View.GONE);
                                    lay = (LinearLayout) findViewById(R.id.layout_edit_current);
                                    lay.setVisibility(View.VISIBLE);
                                    lay = (LinearLayout) findViewById(R.id.layout_image_submit);
                                    lay.setVisibility(View.GONE);
                                }
                            });
                builder.create().show();
            }
        });

        Button submit = (Button) findViewById(R.id.button_submit_image);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String filePath = RemoteDatabase.getPath(act, uri);

                    int cutOff = filePath.lastIndexOf("/");
                    int end = filePath.lastIndexOf(".");

                    String name;
                    if (end != -1) {
                        name = filePath.substring(cutOff != -1 ? cutOff + 1: 0, end);
                    } else {
                        name = filePath.substring(cutOff != -1 ? cutOff + 1 : 0);
                    }
                    name = name.trim() + ".jpg";

                    Bitmap bitmap = image;
                    String path = Image.getAlbumStorageDir("routedb").getPath();
                    File f = new File(path + "/" + name);
                    if (!f.exists()) {
                        f.createNewFile();
                    }

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
                    }


                    Toast.makeText(act, "Attempting to upload image.", Toast.LENGTH_SHORT).show();
                    new SendImageTask((CragChatActivity)act, User.currentToken(act), Uri.fromFile(f), rFinal.getId(), "nocap", null).execute() ;
                    launch(rFinal);
                    bitmap.recycle();
                    original.recycle();
                    image.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void drawPath(Canvas canvas, List<Point> points, Paint paint) {
        Path path = new Path();
        boolean first = true;
        for(Point point : tracePoints){
            if(first){
                first = false;
                path.moveTo(point.x, point.y);
            }
            else{
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
    }

    private int dist(Point one, Point two) {
        int x = Math.abs(two.x - one.x);
        int y = Math.abs(two.y - one.y);
        return (int)Math.sqrt((x * x) + (y * y));
    }

    final float[] getPointerCoords(ImageView view, MotionEvent e)
    {
        final int index = e.getActionIndex();
        final float[] coords = new float[] { e.getX(index), e.getY(index) };
        Matrix matrix = new Matrix();
        view.getImageMatrix().invert(matrix);
        matrix.postTranslate(view.getScrollX(), view.getScrollY());
        matrix.mapPoints(coords);
        return coords;
    }

    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();



        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > 2048 || rotatedHeight > 2048) {
            float widthRatio = ((float) rotatedWidth) / ((float) 2048);
            float heightRatio = ((float) rotatedHeight) / ((float) 2048);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }


}
