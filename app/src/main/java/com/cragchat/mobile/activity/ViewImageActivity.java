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
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.database.models.RealmImage;
import com.cragchat.mobile.database.models.RealmRoute;
import com.cragchat.mobile.fragments.ColorPickerDialog;
import com.cragchat.mobile.model.Image;
import com.cragchat.mobile.util.FormatUtil;
import com.cragchat.mobile.view.ImageEditView;
import com.cragchat.networkapi.ErrorHandlingObserverable;
import com.cragchat.networkapi.NetworkApi;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ViewImageActivity extends CragChatActivity {

    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.caption) TextView caption;
    @BindView(R.id.caption_scrollview) ScrollView scrollView;
    @BindView(R.id.back_button) ImageView back;
    @BindView(R.id.hide_caption) TextView hideCaption;
    @BindView(R.id.caption_layout) RelativeLayout captionLayout;
    @BindView(R.id.show_caption_layout) RelativeLayout showCaptionLayout;
    @BindView(R.id.show_caption_button) TextView showCaptionButton;
    @BindView(R.id.author_name) TextView author;

    private Realm mRealm;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);
        String filename = getIntent().getStringExtra(RealmImage.FIELD_FILENAME);
        String localPath = getIntent().getStringExtra("localPath");
        final String entityKey = getIntent().getStringExtra("entityKey");
        mRealm = Realm.getDefaultInstance();
        RealmImage image = mRealm.where(RealmImage.class).equalTo(RealmImage.FIELD_FILENAME, filename).findFirst();


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

        Uri uri = Uri.parse(localPath);

        Bitmap correctlyOriented = null;
        try {
            correctlyOriented = BitmapFactory.decodeFile(localPath);
        }catch (Exception e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(correctlyOriented);
        if (image.getCaption().isEmpty()) {
            captionLayout.setVisibility(GONE);
        } else {
            caption.setText(image.getCaption());
            author.setText(image.getAuthorName());
        }

        hideCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    captionLayout.setVisibility(GONE);
                    showCaptionLayout.setVisibility(VISIBLE);
            }
        });
        showCaptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captionLayout.setVisibility(VISIBLE);
                showCaptionLayout.setVisibility(GONE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmArea area = mRealm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, entityKey).findFirst();
                if (area != null) {
                    launch(area);
                } else {
                    RealmRoute route = mRealm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, entityKey).findFirst();
                    if (route != null) {
                        launch(route);
                    }
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
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
}
