package com.cragchat.mobile.mvp.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.cragchat.mobile.mvp.view.AppBarCollapseListener;
import com.cragchat.mobile.mvp.view.activity.MainActivity;

public class CollapsingRecyclerView extends RecyclerView {

    public CollapsingRecyclerView(Context context) {
        this(context, null);
    }

    public CollapsingRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private AppBarCollapseListener mAppBarCollapseListener;
    private View mView;
    private int mTopPos;
    private LinearLayoutManager mLayoutManager;

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow,
                                           int type) {

        // App bar latching trouble is only with this type of movement when app bar is expanded
        // or collapsed. In touch mode, everything is OK regardless of the open/closed status
        // of the app bar.
        if (type == ViewCompat.TYPE_NON_TOUCH && mAppBarCollapseListener.isAppBarIdle()
                && isNestedScrollingEnabled()) {
            // Make sure the AppBar stays expanded when it should.
            if (dy > 0) { // swiped up
                if (mAppBarCollapseListener.isAppBarExpanded()) {
                    // Appbar can only leave its expanded state under the power of touch...
                    consumed[1] = dy;
                    return true;
                }
            } else { // swiped down (or no change)
                // Make sure the AppBar stays collapsed when it should.
                // Only dy < 0 will open the AppBar. Stop it from opening by consuming dy if needed.
                Log.d("HUH", "HERE");
                mTopPos = mLayoutManager.findFirstVisibleItemPosition();
                if (mTopPos == 0) {
                    mView = mLayoutManager.findViewByPosition(mTopPos);
                    if (-mView.getTop() + dy <= 0) {
                        // Scroll until scroll position = 0 and AppBar is still collapsed.
                        RecyclerView.LayoutParams pareams = (RecyclerView.LayoutParams) mView.getLayoutParams();
                        consumed[1] = dy - mView.getTop()+ (int)MainActivity.dpToPixels(pareams.topMargin, getContext());
                        return true;
                    }
                }
            }
        }

        boolean returnValue = super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
        // Fix the scrolling problems when scrolling is disabled. This issue existed prior
        // to 26.0.0-beta2.
        if (offsetInWindow != null && !isNestedScrollingEnabled() && offsetInWindow[1] != 0) {
            offsetInWindow[1] = 0;
        }
        return returnValue;
    }

    @Override
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        super.setLayoutManager(layout);
        mLayoutManager = (LinearLayoutManager) getLayoutManager();
    }

    public void setAppBarCollapseListener(AppBarCollapseListener AppBarCollapseListener) {
        mAppBarCollapseListener = AppBarCollapseListener;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "MyRecyclerView";
}