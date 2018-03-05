package com.cragchat.mobile.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.cragchat.mobile.data.authentication.Authentication;
import com.cragchat.mobile.data.Repository;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class CragChatActivity extends DaggerAppCompatActivity {

    @Inject
    public Repository repository;

    @Inject
    public Authentication authentication;

    private final PopupMenu.OnMenuItemClickListener menuListener = new PopupMenu.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = null;
            if (intent != null) {
                startActivity(intent);
            }
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.more) {
            openPopupMenu(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean openPopupMenu(MenuItem menuItem) {
        int layout = authentication.isLoggedIn(this) ? R.menu.menu_profile : R.menu.menu_not_logged_in;
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.more));
        popup.getMenuInflater().inflate(layout, popup.getMenu());
        popup.setOnMenuItemClickListener(menuListener);
        popup.show();
        return true;
    }

}
