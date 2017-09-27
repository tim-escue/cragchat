package com.cragchat.mobile.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.media.ExifInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Image;
import com.cragchat.mobile.fragments.ColorPickerDialog;
import com.cragchat.mobile.fragments.NotificationDialog;
import com.cragchat.mobile.sql.LocalDatabase;
import com.cragchat.mobile.sql.SendCommentTask;
import com.cragchat.mobile.sql.SendImageTask;
import com.cragchat.mobile.user.User;
import com.cragchat.mobile.view.ImageEditView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class EditImageActivity extends CragChatActivity implements ColorPickerDialog.OnColorChangedListener {

    private Displayable displayable;
    private String captionString;

    @BindView(R.id.image_edit_view)
    ImageEditView imageEditView;

    @BindView(R.id.button_paint)
    ImageView paint;

    @BindView(R.id.button_done)
    ImageView done;

    @BindView(R.id.button_color_picker)
    ImageView pickColor;

    @BindView(R.id.button_undo)
    ImageView undo;

    @BindView(R.id.button_stroke)
    ImageView stroke;

    @BindView(R.id.button_caption)
    ImageView caption;

    @BindView(R.id.button_submit_image)
    ImageView submit;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_image_edit);
        ButterKnife.bind(this);
        displayable = LocalDatabase.getInstance(this).findExact(getIntent().getIntExtra("displayable_id", -1));

        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(uiOptions);
                }
            }
        });
        decorView.setSystemUiVisibility(uiOptions);

        Uri uri = Uri.parse(getIntent().getStringExtra("image_uri"));

        Bitmap bitmap = getBitmap(uri, getContentResolver());

        imageEditView.setImageBitmap(bitmap);

    }


    private static int getRotation(InputStream stream) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(stream);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        int orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return -1;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    private static Bitmap getBitmap(Uri uri, ContentResolver resolver) {
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = resolver.openInputStream(uri);
            int rotation = getRotation(in);
            in.close();
            in = resolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();


            int scale = 1;
            while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap resultBitmap = null;
            in = resolver.openInputStream(uri);
            if (scale > 1) {
                scale--;
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                resultBitmap = BitmapFactory.decodeStream(in, null, options);

                int height = resultBitmap.getHeight();
                int width = resultBitmap.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(resultBitmap, (int) x,
                        (int) y, true);
                resultBitmap.recycle();
                resultBitmap = scaledBitmap;

                System.gc();
            } else {
                resultBitmap = BitmapFactory.decodeStream(in);
            }
            in.close();
            if (rotation != -1) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                Bitmap rotated = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);
                resultBitmap.recycle();
                return rotated;
            }

            Log.d("Image size:", resultBitmap.getHeight() * resultBitmap.getWidth() * 4 + "");
            return resultBitmap;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    @OnClick(R.id.button_submit_image)
    public void onSubmit() {
        File directory = Image.getAlbumStorageDir("routedb");
        Bitmap bitmap = imageEditView.scaleDown();
        File newFile = new File(directory, bitmap.hashCode()+".png");
        FileOutputStream out = null;
        try {
            Log.d("CREATEDFILE", newFile.getPath());

            if (newFile.exists()) {
                newFile.createNewFile();
            }
            out = new FileOutputStream(newFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                new SendImageTask(this, User.currentToken(this), Uri.fromFile(newFile), displayable.getId(),  captionString, null).execute();
           launch(displayable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.button_caption)
    public void onClickCaption() {
        showCaptionDIalog(new EditText(this));
    }

    void showCaptionDIalog(final EditText txtUrl) {
        AlertDialog dialogParent = new AlertDialog.Builder(this)
                .setTitle("Add Caption")
                .setView(txtUrl)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = txtUrl.getText().toString().trim();
                        if (!url.equals("")) {
                            captionString = url;
                        } else {
                            Toast.makeText(EditImageActivity.this, "Can't add empty comment", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final AlertDialog dialog1 = new AlertDialog.Builder(EditImageActivity.this)
                                .setMessage("Are you sure you want to delete this caption?")
                                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        final EditText newt = new EditText(EditImageActivity.this);
                                        newt.setText(txtUrl.getText());
                                        dialog.dismiss();
                                        showCaptionDIalog(newt);                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                })
                                .create();
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.show();
                    }
                }).create();
        dialogParent.setCanceledOnTouchOutside(false);
        dialogParent.show();
    }

    @OnClick(R.id.button_paint)
    public void onClickPaint() {
        imageEditView.setDrawing(true);
        paint.setVisibility(GONE);
        caption.setVisibility(GONE);
        submit.setVisibility(GONE);
        done.setVisibility(View.VISIBLE);
        pickColor.setVisibility(View.VISIBLE);
        undo.setVisibility(View.VISIBLE);
        stroke.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_color_picker)
    public void onClickColorPicker() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(Color.RED)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("select", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        imageEditView.setDrawColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @OnClick(R.id.button_undo)
    public void onClickUndo() {
        imageEditView.undoLast();
    }

    @OnClick(R.id.button_done)
    public void onClickDone() {
        done.setVisibility(GONE);
        stroke.setVisibility(GONE);
        pickColor.setVisibility(GONE);
        undo.setVisibility(GONE);
        submit.setVisibility(View.VISIBLE);
        paint.setVisibility(View.VISIBLE);
        caption.setVisibility(View.VISIBLE);
        imageEditView.setDrawing(false);
    }

    @OnClick(R.id.button_stroke)
    public void onClickStroke() {
        final Dialog d = new Dialog(this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_number_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(60);
        np.setValue(20);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                imageEditView.setStroke(np.getValue());
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void colorChanged(int color) {
        imageEditView.setDrawColor(color);
    }
}
