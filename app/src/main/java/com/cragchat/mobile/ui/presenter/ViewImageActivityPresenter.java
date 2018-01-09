package com.cragchat.mobile.ui.presenter;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cragchat.mobile.R;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Image;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.view.activity.ViewImageActivity;
import com.cragchat.mobile.util.FileUtil;
import com.cragchat.mobile.util.NavigationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ViewImageActivityPresenter {

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

    private Image image;

    @OnClick(R.id.back_button)
    void back(View view) {
        Area area = Repository.getArea(image.getEntityKey(), null);
        if (area != null) {
            NavigationUtil.launch(view.getContext(), area);
        } else {
            Route route = Repository.getRoute(image.getEntityKey(), null);
            if (route != null) {
                NavigationUtil.launch(view.getContext(), route);
            }
        }
    }

    public ViewImageActivityPresenter(ViewImageActivity activity, Image image) {
        this.image = image;
        ButterKnife.bind(this, activity);
        final View decorView = activity.getWindow().getDecorView();
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

}