package com.cragchat.mobile.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.domain.model.Area;
import com.cragchat.mobile.domain.model.Image;
import com.cragchat.mobile.domain.model.Route;import com.cragchat.mobile.util.FileUtil;
import com.cragchat.mobile.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ViewImageActivity extends CragChatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.caption)
    TextView caption;
    @BindView(R.id.caption_scrollview)
    ScrollView scrollView;
    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.hide_caption)
    TextView hideCaption;
    @BindView(R.id.caption_layout)
    RelativeLayout captionLayout;
    @BindView(R.id.show_caption_layout)
    RelativeLayout showCaptionLayout;
    @BindView(R.id.show_caption_button)
    TextView showCaptionButton;
    @BindView(R.id.author_name)
    TextView author;

    Image image;

    @OnClick(R.id.back_button)
    void back(View view) {
        Area area = repository.getArea(image.getEntityKey());
        if (area != null) {
            ViewUtil.launch(view.getContext(), area.getKey());
        } else {
            Route route = repository.getRoute(image.getEntityKey());
            if (route != null) {
                ViewUtil.launch(view.getContext(), route);
            }
        }
    }

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);
        image = getIntent().getParcelableExtra(ViewUtil.IMAGE);
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

        FileUtil.loadIntoImageView(image.getFilename(), imageView, progressBar);

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
