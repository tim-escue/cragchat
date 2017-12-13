package com.cragchat.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;

public class CragChatActivity extends AppCompatActivity {

    public static final String DATA_STRING = "DATA_STRING";
    PopupMenu.OnMenuItemClickListener menuListener = new PopupMenu.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = null;
            System.out.println("listener in cragchatactivity");
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

    public void launch(Area area) {
        launch(area, 0);
    }

    public void launch(Area area, int tab) {
        Intent intent = new Intent(this, AreaActivity.class);
        intent.putExtra(DATA_STRING, area.getKey());
        intent.putExtra("TAB", tab);
        startActivity(intent);
    }

    public void launch(Route route) {
        launch(route, 0);
    }

    public void launch(Route route, int tab) {
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtra(DATA_STRING, route.getKey());
        intent.putExtra("TAB", tab);
        startActivity(intent);
    }
}
