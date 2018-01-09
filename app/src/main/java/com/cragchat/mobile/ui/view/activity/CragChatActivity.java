package com.cragchat.mobile.ui.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;

public class CragChatActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.more) {
            openPopupMenu(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean openPopupMenu(MenuItem menuItem) {
        int layout = Authentication.isLoggedIn(this) ? R.menu.menu_profile : R.menu.menu_not_logged_in;
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.more));
        popup.getMenuInflater().inflate(layout, popup.getMenu());
        popup.setOnMenuItemClickListener(menuListener);
        popup.show();
        return true;
    }


}
