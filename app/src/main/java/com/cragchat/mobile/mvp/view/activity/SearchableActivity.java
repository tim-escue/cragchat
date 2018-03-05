package com.cragchat.mobile.mvp.view.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cragchat.mobile.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by timde on 11/9/2017.
 */

public abstract class SearchableActivity extends CragChatActivity {

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        ButterKnife.bind(this);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchableActivity.this, SearchActivity.class);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.search);
        if (item == null) {
            getMenuInflater().inflate(R.menu.menu_main_activity, menu);
            item = menu.findItem(R.id.search);
        }

        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

}
