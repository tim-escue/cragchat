package com.cragchat.mobile.ui.view.activity;

import android.os.Bundle;
import android.view.View;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.model.Image;
import com.cragchat.mobile.ui.presenter.ViewImageActivityPresenter;
import com.cragchat.mobile.util.NavigationUtil;

public class ViewImageActivity extends CragChatActivity {

    private ViewImageActivityPresenter presenter;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_view_image);

        presenter = new ViewImageActivityPresenter(this, (Image) getIntent().getParcelableExtra(NavigationUtil.IMAGE));
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
