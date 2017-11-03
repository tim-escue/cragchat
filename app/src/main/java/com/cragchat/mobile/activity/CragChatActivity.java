package com.cragchat.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.database.models.RealmRoute;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Displayable;
import com.cragchat.mobile.model.LegacyRoute;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.user.User;

public class CragChatActivity extends AppCompatActivity {

    public static final String DATA_STRING = "DATA_STRING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            
        } else if (item.getItemId() == R.id.more) {
            openPopupMenu(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean openPopupMenu(MenuItem menuItem) {
        int layout = User.currentToken(this) != null ? R.menu.menu_profile : R.menu.menu_not_logged_in;
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.more));
        popup.getMenuInflater().inflate(layout, popup.getMenu());
        popup.setOnMenuItemClickListener(menuListener);
        popup.show();
        return true;
    }

    PopupMenu.OnMenuItemClickListener menuListener = new PopupMenu.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = null;
            String selection = item.getTitle().toString();
            switch (selection) {
                case "My Profile":
                    intent = new Intent(CragChatActivity.this, ProfileActivity.class);
                    intent.putExtra("username", User.userName(CragChatActivity.this));
                    break;
                case "Log out":
                    User.logout(CragChatActivity.this);
                    intent = new Intent(CragChatActivity.this, MainActivity.class);
                    break;
                case "Login":
                    intent = new Intent(CragChatActivity.this, LoginActivity.class);
                    break;
            }
            if (intent != null) {
                startActivity(intent);
            }
            return true;
        }
    };

    public void launch(Area area) {
        launch(area, 0);
    }

    public void launch(RealmArea area) {
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

    public void launch(RealmRoute route) {
        launch(route, 0);
    }


    public void launch(Route route, int tab) {
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtra(DATA_STRING, route.getKey());
        intent.putExtra("TAB", tab);
        startActivity(intent);
    }

    public void launch(Displayable r) {
        launch(r, 0);
    }

    public void launch(Displayable r, int tab) {
        Intent intent;
        String encoded;

        if (r instanceof LegacyRoute) {
            intent = new Intent(this, RouteActivity.class);
            encoded = r.toString();
        } else {
            intent = new Intent(this, AreaActivity.class);
            encoded = r.getName();
        }
        intent.putExtra("TAB", tab);
        intent.putExtra(DATA_STRING, encoded);
        startActivity(intent);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
