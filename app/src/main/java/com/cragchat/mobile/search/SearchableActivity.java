package com.cragchat.mobile.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.activity.LoginActivity;
import com.cragchat.mobile.activity.MainActivity;
import com.cragchat.mobile.activity.ProfileActivity;
import com.cragchat.mobile.user.User;

public class SearchableActivity extends CragChatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public boolean startSearch(MenuItem menuItem) {
        return true;
    }


    public boolean launchProfile(MenuItem menuItem) {
        final Activity act = this;
        if (User.currentToken(this) != null) {
            PopupMenu popup = new PopupMenu(this, findViewById(R.id.user_menu));
            popup.getMenuInflater().inflate(R.menu.menu_profile, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = null;
                    String selection = item.getTitle().toString();
                    switch(selection) {
                        case "My Profile":
                            intent = new Intent(act, ProfileActivity.class);
                            intent.putExtra("username", User.userName(act));
                            break;
                        case "Log out":
                            User.logout(act);
                            intent = new Intent(act, MainActivity.class);
                            break;
                    }
                    startActivity(intent);
                    return true;
                }
            });

            popup.show();//showing popup menu

        } else {
            if (!(this instanceof LoginActivity)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

        }

        return true;
    }
}
